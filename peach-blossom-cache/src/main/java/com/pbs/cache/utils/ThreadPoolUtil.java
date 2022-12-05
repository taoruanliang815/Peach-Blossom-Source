package com.pbs.cache.utils;


import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程池工具
 *
 * @author taoruanliang
 * @date 2022/12/5 13:55
 */
public class ThreadPoolUtil {

    private static ConcurrentHashMap<String, ExecutorService> THREAD_POOL_MAP = new ConcurrentHashMap<>();

    private static final AtomicInteger ATOMIC_THREAD_COUNT = new AtomicInteger(0);

    public synchronized static ExecutorService getThreadPoll(String key) {
        if (!THREAD_POOL_MAP.containsKey(key)) {
            ExecutorService THREAD_POLL = new ThreadPoolExecutor(100, 1000, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue(5000),
                    runnable -> new Thread(runnable, key + ATOMIC_THREAD_COUNT.incrementAndGet()));
            THREAD_POOL_MAP.put(key, THREAD_POLL);
        }
        return THREAD_POOL_MAP.get(key);
    }

}


