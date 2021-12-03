package org.zhangyj.db.select;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;
import org.zhangyj.db.bean.StopWatcher;
import org.zhangyj.db.bean.entity.InsertMsg;
import org.zhangyj.db.insert.mapper.InsertMsgMapper;
import org.zhangyj.db.insert.service.InsertMsgService;
import org.zhangyj.db.insert.service.impl.InsertMsgServiceImpl;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class TestSelect {
    int pageSize = 20000;

    @Autowired
    private InsertMsgService insertMsgService;

    @Test
    public void testSelectMsg() throws Exception {
//        CompletableFuture.completedFuture()
        AtomicInteger i = new AtomicInteger(0);
        StopWatch stopWatch = StopWatcher.watch(() -> {
            // 单线程：main查询条数：5000000 分页条数：20000 总耗时：52 速度：94974/s
            pageInsertMsg(i);

        });
        log.info(Thread.currentThread().getName() + "查询条数：{} 分页条数：{} 总耗时：{} 速度：{}/s", i.get(), pageSize, (int)stopWatch.getTotalTimeSeconds(), (int)(i.get()/stopWatch.getTotalTimeSeconds()));
    }

    private void pageInsertMsg(AtomicInteger i) {
        Page<InsertMsg> page;
        long minId = 0;
        List<InsertMsg> records;
        do{
            LambdaQueryWrapper<InsertMsg> wrapper = new LambdaQueryWrapper<>();
            wrapper.gt(InsertMsg::getId, minId);
            page = new Page<>();
            page.setSearchCount(false);
            page.setAsc("id");
            page.setSize(pageSize);
            page.setCurrent(1);
            insertMsgService.page(page, wrapper);
            records = page.getRecords();
            if(CollectionUtils.isEmpty(records)){
                break;
            }
            int count = i.addAndGet(records.size());
            minId = records.get(records.size() - 1).getId();
            log.info("查询条数：{} 当前分页：{}", count, page.getCurrent());
        }while (records.size() == page.getSize());
    }
}
