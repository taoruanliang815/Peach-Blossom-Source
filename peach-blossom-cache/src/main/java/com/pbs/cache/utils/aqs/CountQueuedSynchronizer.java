package com.pbs.cache.utils.aqs;

import com.pbs.cache.utils.tuple.Tuple2;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author taoruanliang
 * @date 2022/2/17 10:43
 */
@Slf4j
public class CountQueuedSynchronizer {

    private final Sync sync;

    private final String name;

    /**
     * 等待的队列
     */
    private final LinkedBlockingQueue<Tuple2<Thread, LocalDateTime>> waitQueue = new LinkedBlockingQueue<>();
    /**
     * 持有的队列
     */
    private final Map<Thread, LocalDateTime> triggerMap = new ConcurrentHashMap<>();

    private final int count;

    private final ReentrantLock reentrantLock = new ReentrantLock(false);

    public CountQueuedSynchronizer(int count, String name) {
        if (count < 0) {
            throw new IllegalArgumentException("count < 0");
        } else {
            this.name = name;
            this.count = count;
            this.sync = new Sync(count);
        }
    }

    /**
     * 释放
     *
     * @param
     * @return
     * @description:
     */
    public void release() {
        LocalDateTime holdTime = triggerMap.remove(Thread.currentThread());

        if (Objects.isNull(holdTime)) {
            return;
        }

        log.info("[CountQueuedSynchronizer][release] syncName:{},holdTime:{},releaseTime:{}", name, holdTime, LocalDateTime.now());
        this.sync.rollbackShared(count);

        if (reentrantLock.tryLock()) {
            try {
                while (!CollectionUtils.isEmpty(waitQueue) && triggerMap.size() < count) {
                    Tuple2<Thread, LocalDateTime> poll = waitQueue.poll();
                    if (Objects.nonNull(poll)) {
                        LockSupport.unpark(poll.getKey());
                    }
                }
            } finally {
                reentrantLock.unlock();
            }
        }
    }


    /**
     * 持有
     *
     * @param
     * @return
     * @description:
     */
    @SneakyThrows
    public Boolean hold(LocalDateTime holdTime) {
        boolean releaseShared = this.sync.tryReleaseShared(1);
        //获取成功则存入map
        if (releaseShared) {
            triggerMap.put(Thread.currentThread(), holdTime);
            return true;
        } else {
            log.info("hold fail:holdTime:{},successList:{}", holdTime,triggerMap.keySet());
            //第二次会不会失败？答案是应该不会
            Tuple2<Thread, LocalDateTime> with = Tuple2.with(Thread.currentThread(), holdTime);
            waitQueue.offer(with);
            LockSupport.park();
            waitQueue.remove(with);
            LocalDateTime now = LocalDateTime.now();
            log.info("re hold:preHoldTime:{},newHoldTime:{}", holdTime, now);
            return hold(now);
        }

    }

    public long getCount() {
        return this.sync.getCount();
    }


    @Override
    public String toString() {
        return super.toString() + "[Count = " + this.sync.getCount() + ",Wait = " + waitQueue.size() +
                ",Hold = " + triggerMap.size() + "]";
    }


    /**
     * 内部aqs
     *
     * @author : longchuan
     * @version :
     * @date : 2021/12/27 1:35 下午
     * @description:
     */
    private static final class Sync extends AbstractQueuedSynchronizer {
        private static final long serialVersionUID = -1L;

        Sync(int count) {
            this.setState(count);
        }

        int getCount() {
            return this.getState();
        }

        Boolean rollbackShared(int maxV) {
            int c;
            int nextc;
            do {
                c = this.getState();
                if (c == maxV) {
                    return false;
                }

                nextc = c + 1;
            } while (!this.compareAndSetState(c, nextc));

            return Boolean.TRUE;
        }

        /**
         * 尝试获取
         *
         * @param
         * @return
         * @description:
         */
        @Override
        protected int tryAcquireShared(int acquires) {
            return this.getState() == 0 ? 1 : -1;
        }

        /**
         * 尝试释放
         *
         * @param
         * @return
         * @description:
         */
        @Override
        protected boolean tryReleaseShared(int releases) {
            int c;
            int nextc;
            do {
                c = this.getState();
                if (c == 0) {
                    return false;
                }

                nextc = c - 1;
            } while (!this.compareAndSetState(c, nextc));

            return Boolean.TRUE;
        }
    }
}
