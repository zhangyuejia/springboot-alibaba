package com.montnets.redis.redis;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 漏斗限流
 * @author zhangyj
 */
@Component
public class FunnelRateLimiter {

    static class Funnel {

        int capacity;

        float leakingRate;

        /**
         * 剩下空间
         */
        int leftQuota;
        /**
         * 漏时间
         */
        long leakingTs;


        public Funnel(int capacity, float leakingRate) {

            this.capacity = capacity;

            this.leakingRate = leakingRate;

            this.leftQuota = capacity;

            this.leakingTs = System.currentTimeMillis();

        }

        void makeSpace() {
            long nowTs = System.currentTimeMillis();
            // 时间间隔
            long deltaTs = nowTs - leakingTs;
            // 腾出空间
            int deltaQuota = (int) (deltaTs * leakingRate);
            // 间隔时间太长，整数数字过大溢出
            if (deltaQuota < 0) {
                // 重置漏斗
                this.leftQuota = capacity;
                this.leakingTs = nowTs;
                return;
            }
            // 腾出空间太小，最小单位是 1
            if (deltaQuota < 1) {
                return;
            }
            // 剩余空间
            this.leftQuota += deltaQuota;
            this.leakingTs = nowTs;
            if (this.leftQuota > this.capacity) {
                this.leftQuota = this.capacity;
            }
        }

        boolean watering(int quota) {
            // 腾出空间
            makeSpace();
            if (this.leftQuota >= quota) {
                this.leftQuota -= quota;
                return true;
            }
            return false;
        }
    }

    private Map<String, Funnel> funnels = new HashMap<>();

    public boolean isActionAllowed(String userId, String actionKey, int capacity, float leakingRate) {
//        漏斗流速：leakingRate 单位：个/毫秒
        String key = String.format("%s:%s", userId, actionKey);
        // 获取漏斗
        Funnel funnel = funnels.get(key);
        if (funnel == null) {
            funnel = new Funnel(capacity, leakingRate);
            funnels.put(key, funnel);
        }
        // 需要 1 个 quota
        return funnel.watering(1);
    }

}