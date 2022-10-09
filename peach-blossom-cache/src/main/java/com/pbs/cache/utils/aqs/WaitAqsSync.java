package com.pbs.cache.utils.aqs;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.function.Supplier;

/**
 * @author taoruanliang
 * @date 2022/2/17 10:43
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WaitAqsSync {

    /**
     * 增强aqs 同进程内只允许N个线程执行该动作
     * @param
     * @return
     * @description:
     */
    public static <R> R wrapper(Supplier<R> supplier, CountQueuedSynchronizer countQueuedSynchronizer) {
        try {
            //注意：拿不到就wait，谨慎估计，范围尽量小
            countQueuedSynchronizer.hold(LocalDateTime.now());
            return supplier.get();
        } finally {
            countQueuedSynchronizer.release();
        }
    }


}
