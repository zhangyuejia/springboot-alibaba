package org.zhangyj.db.select;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.ResultSetType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StopWatch;
import org.zhangyj.db.bean.StopWatcher;
import org.zhangyj.db.bean.entity.InsertMsg;
import org.zhangyj.db.insert.mapper.InsertMsgMapper;
import org.zhangyj.db.insert.service.InsertMsgService;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.atomic.AtomicInteger;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class TestSelect {

    private InsertMsgMapper insertMsgMapper;

    @Autowired
    private InsertMsgService insertMsgService;

    @Test
    public void testSelectMsg() throws Exception {
        AtomicInteger i = new AtomicInteger(0);
        StopWatch stopWatch = StopWatcher.watch(() -> {
            // main查询条数：5000000 分页条数：20000 总耗时：75 速度：66653/s
            // main查询条数：50000000 分页条数：20000 总耗时：533 速度：93638/s
            insertMsgService.pageInsertMsg(i, 0L, null);

            // fork/join: 速度在单线程2倍以上
            // main查询条数：5000000 分页条数：20000 总耗时：40 速度：123703/s
            // main查询条数：50000000 分页条数：20000 总耗时：266 速度：187713/s
            // 流式查询 main查询条数：50000000 分页条数：20000 总耗时：243 速度：205440/s
            ForkJoinTask<List<InsertMsg>> task = new SelectMsgTask(insertMsgService, i, 0L, 5000*10000L);
            List<InsertMsg> result = ForkJoinPool.commonPool().invoke(task);
//            insertMsgService.flowPageInsertMsg(i, 0, 500000);
        });
        log.info(Thread.currentThread().getName() + "查询条数：{} 分页条数：{} 总耗时：{} 速度：{}/s", i.get(), InsertMsgService.pageSize, (int)stopWatch.getTotalTimeSeconds(), (int)(i.get()/stopWatch.getTotalTimeSeconds()));
    }



    @RequiredArgsConstructor
    static class SelectMsgTask extends RecursiveTask<List<InsertMsg>> {

        private static final int THRESHOLD = 10 * 10000;

        private final InsertMsgService insertMsgService;

        private final AtomicInteger i;

        private final long minId;

        private final long maxId;
        
        @Override
        protected List<InsertMsg> compute() {
            if (maxId - minId <= THRESHOLD) {
//                return insertMsgService.pageInsertMsg(i, minId, maxId);
                return insertMsgService.flowPageInsertMsg(i, minId, maxId);
            }
            // 任务太大,一分为二:
            long middle = (maxId + minId) / 2;
            SelectMsgTask task1 = new SelectMsgTask(insertMsgService, i, minId, middle);
            SelectMsgTask task2 = new SelectMsgTask(insertMsgService, i, middle, maxId);
            invokeAll(task1, task2);
            List<InsertMsg> list1 = task1.join();
            List<InsertMsg> list2 = task2.join();
//            list1.addAll(list2);
            return list1;
        }
    }
}
