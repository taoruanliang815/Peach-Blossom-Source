package com.pbs.cache.utils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.function.Supplier;

/**
 * 限流工具
 *
 * @author taoruanliang
 * @date 2022/12/5 13:55
 */
public class LimitUtil {

    private static final ConcurrentHashMap<String, LimitUtil.Limit> limitMap = new ConcurrentHashMap<>();

    public static class Limit {

        private Semaphore semaphore;

        Limit(Integer permits) {
            semaphore = new Semaphore(permits, true);
        }

        public void lock() {
            System.out.println("当前线程数:" + semaphore.availablePermits() + ",当前等待数:" + semaphore.getQueueLength());
            semaphore.tryAcquire();
        }

        public void unLock() {
            semaphore.release();
        }
    }

    public synchronized static Limit get(String code, Integer permits) {
        Limit limit = limitMap.get(code);
        if (limit == null) {
            limit = new Limit(permits);
            limitMap.put(code, limit);
        }
        return limit;
    }

    public static <R> R limit(Supplier<R> supplier, String businessCode, Integer permits) {
        Limit limit = get(businessCode, permits);
        try {
            limit.lock();
            return supplier.get();
        } finally {
            limit.unLock();
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i <= 100; i++) {
            int finalI = i;
            ThreadPoolUtil.getThreadPoll("a").execute(() -> {
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(LimitUtil.limit(() -> finalI, "code", 5));
            });
        }
    }
}
