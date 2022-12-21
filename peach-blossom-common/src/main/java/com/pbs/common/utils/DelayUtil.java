package com.pbs.common.utils;

import io.netty.util.HashedWheelTimer;
import io.netty.util.Timer;
import io.netty.util.TimerTask;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 延时处理工具
 *
 * @author taoruanliang
 * @date 2022/10/24 14:26
 */
@Component
public class DelayUtil implements InitializingBean {

    public static Timer timer;

    @Override
    public void afterPropertiesSet() throws Exception {
        timer = new HashedWheelTimer(
                1,
                TimeUnit.MINUTES,
                1024);
    }

    public static void newTimeout(TimerTask task, long delay, TimeUnit unit) {
        timer.newTimeout(task, delay, unit);
    }
}
