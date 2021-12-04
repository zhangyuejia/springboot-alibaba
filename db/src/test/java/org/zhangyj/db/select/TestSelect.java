package org.zhangyj.db.select;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StopWatch;
import org.zhangyj.db.bean.StopWatcher;
import org.zhangyj.db.bean.entity.InsertMsg;
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


    @Autowired
    private InsertMsgService insertMsgService;

    @Test
    public void testSelectMsg() throws Exception {
        AtomicInteger i = new AtomicInteger(0);
        StopWatch stopWatch = StopWatcher.watch(() -> {
//            // 单线程：main查询条数：5000000 分页条数：20000 总耗时：52 速度：94974/s
            // main查询条数：5000000 分页条数：20000 总耗时：75 速度：66653/s
            // main查询条数：5000000 分页条数：20000 总耗时：77 速度：64375/s
//            insertMsgService.pageInsertMsg(i, 0, -1);

            // fork/join:
            // main查询条数：5000000 分页条数：20000 总耗时：37 速度：132024/s
            // main查询条数：5000000 分页条数：20000 总耗时：40 速度：123703/s
            ForkJoinTask<List<InsertMsg>> task = new SelectMsgTask(insertMsgService, i, 0L, 500*10000L);
            List<InsertMsg> result = ForkJoinPool.commonPool().invoke(task);

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
                return insertMsgService.pageInsertMsg(i, minId, maxId);
            }
            // 任务太大,一分为二:
            long middle = (maxId + minId) / 2;
            SelectMsgTask task1 = new SelectMsgTask(insertMsgService, i, minId, middle);
            SelectMsgTask task2 = new SelectMsgTask(insertMsgService, i, middle, maxId);
            invokeAll(task1, task2);
            List<InsertMsg> list1 = task1.join();
            List<InsertMsg> list2 = task2.join();
            list1.addAll(list2);
            return list1;
        }
    }
}
