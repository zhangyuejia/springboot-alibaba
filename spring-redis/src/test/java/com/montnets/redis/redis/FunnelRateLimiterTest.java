package com.montnets.redis.redis;

import com.montnets.redis.SpringRedisApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringRedisApplication.class)
public class FunnelRateLimiterTest {

    @Resource
    private FunnelRateLimiter limiter;

    @Test
    public void test(){
        for (int i = 0; i < 20; i++) {
            boolean actionAllowed = limiter.isActionAllowed("laoqian", "reply", 60, 5);
            log.info(String.valueOf(actionAllowed));
        }
    }
}
