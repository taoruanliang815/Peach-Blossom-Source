package com.pbs.cache.annotation;

import java.lang.annotation.*;

/**
 * 缓存注解
 *
 * @author taoruanliang
 * @date 2022/2/16 13:49
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface BlossomCache {

    /**
     * 业务key
     *
     * @return
     */
    String businessKey() default "businessKey";

    /**
     * 缓存key
     *
     * @return
     */
    String cacheKey();

    /**
     * 是否通过MQ远程同步缓存
     *
     * @return
     */
    boolean isRemote() default false;
}
