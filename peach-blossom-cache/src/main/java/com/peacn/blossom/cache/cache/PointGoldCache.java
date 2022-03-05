package com.peacn.blossom.cache.cache;

import java.lang.annotation.*;

/**
 * @author taoruanliang
 * @date 2022/2/16 13:49
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface PointGoldCache {

    PointGoldCacheKeyEnum key();

    PointGoldCacheTypeEnum type();

    PointGoldCacheManagerEnum cacheManager() default PointGoldCacheManagerEnum.LOCAL_MANAGER;
}
