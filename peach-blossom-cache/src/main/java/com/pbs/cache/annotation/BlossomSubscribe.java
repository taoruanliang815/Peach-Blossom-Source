package com.pbs.cache.annotation;


import com.pbs.cache.event.Event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author taoruanliang
 * @date 2022/4/22 16:19
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface BlossomSubscribe {

    Class<? extends Event> eventType();
}

