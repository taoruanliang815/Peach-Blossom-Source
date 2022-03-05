package com.peacn.blossom.cache.cache;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author taoruanliang
 * @date 2022/2/16 14:29
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(PointGoldCacheConfiguration.class)
public @interface EnablePointGoldCache {

}
