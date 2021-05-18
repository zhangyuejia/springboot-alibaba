package com.montnets.redis.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

/**
 * 简单限流
 * @author zhangyj
 */
@Component
@RequiredArgsConstructor
public class SimpleRateLimiter {

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 简单限流
     * @param userId 用户id
     * @param actionKey 操作key
     * @param period 周期
     * @param maxCount 最大处理个数
     * @return 操作是否允许（是否被限流）
     */
    public boolean isActionAllowed(String userId, String actionKey, int period, int maxCount) {
        String key = String.format("hist:%s:%s", userId, actionKey);
        long nowTs = System.currentTimeMillis();

        ZSetOperations<String, Object> ops = redisTemplate.opsForZSet();
        ops.removeRangeByScore(key, 0, nowTs - period * 1000L);
        Long zCard = ops.zCard(key);

        boolean actionAllowed = zCard != null && zCard < maxCount;
        if(actionAllowed){
            ops.add(key, String.valueOf(nowTs), nowTs);
        }
        return actionAllowed;
    }
}