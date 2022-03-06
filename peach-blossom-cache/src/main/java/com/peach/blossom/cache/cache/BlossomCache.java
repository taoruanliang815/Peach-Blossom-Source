package com.peach.blossom.cache.cache;

import java.lang.annotation.*;

/**
 * @author taoruanliang
 * @date 2022/2/16 13:49
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface BlossomCache {

    BlossomCacheKeyEnum key();

    BlossomCacheTypeEnum type();

    BlossomCacheManagerEnum cacheManager() default BlossomCacheManagerEnum.LOCAL_MANAGER;
}
