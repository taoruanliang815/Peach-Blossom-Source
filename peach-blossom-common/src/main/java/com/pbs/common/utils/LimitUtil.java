package com.pbs.common.utils;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.LockSupport;
import java.util.function.Supplier;

/**
 * 限流工具
 *
 * @author taoruanliang
 * @date 2022/12/5 13:55
 */
public class LimitUtil {

    private static final ConcurrentHashMap<String, Limit> limitMap = new ConcurrentHashMap<>();

    public static class Limit {

        private Semaphore semaphore;
        private ArrayBlockingQueue<Thread> queue;

        Limit(Integer permits) {
            semaphore = new Semaphore(permits, true);
            queue = new ArrayBlockingQueue<>(1024, true);
        }

        public void lock() {
            Boolean flag = semaphore.tryAcquire();
            if (!flag) {
                Thread thread = Thread.currentThread();
                queue.offer(thread);
                LockSupport.park(thread);
                queue.remove(thread);
                lock();
            }
        }

        public void unLock() {
            semaphore.release();
            Thread thread = queue.poll();
            LockSupport.unpark(thread);
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
                System.out.println(LimitUtil.limit(() -> {
                    try {
                        Thread.sleep(200L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return finalI;
                }, "code", 5));
            });
        }
    }
}
