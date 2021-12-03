package org.zhangyj.db.insert;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StopWatch;
import org.zhangyj.db.bean.StopWatcher;
import org.zhangyj.db.insert.mapper.InsertMsgMapper;
import org.zhangyj.db.insert.service.InsertMsgService;
import org.zhangyj.db.insert.service.impl.InsertMsgServiceImpl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class TestInsert {

    @Autowired
    private InsertMsgMapper insertMsgMapper;

    @Autowired
    private InsertMsgService insertMsgService;

    @Before
    public void before(){
        log.info("清理数据 truncate table");
        insertMsgMapper.truncateTable();
    }

    @Test
    public void testInsertMsg() throws Exception {
        int threadCount = InsertMsgService.threadCount;
        int count = 100 * 10000;
        StopWatch stopWatch = StopWatcher.watch(() -> {
            ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
            for (int i = 0; i < threadCount; i++) {
                executorService.execute(() -> {
                    try {
                        insertMsgService.insertMsg(count / threadCount);
                    } catch (Exception e) {
                        log.error(Thread.currentThread() + "插入报错", e);
                    }
                });
            }
            InsertMsgServiceImpl.latch.await();
        });
        log.info(Thread.currentThread().getName() + "插入条数：{} 总耗时：{} 速度：{}/s", count, (int)stopWatch.getTotalTimeSeconds(), (int)(count/stopWatch.getTotalTimeSeconds()));
    }
}
