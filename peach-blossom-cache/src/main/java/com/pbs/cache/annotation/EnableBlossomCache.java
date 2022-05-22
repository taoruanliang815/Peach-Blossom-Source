package com.pbs.cache.annotation;

import com.pbs.cache.config.BlossomCacheConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author taoruanliang
 * @date 2022/2/16 14:29
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(BlossomCacheConfiguration.class)
public @interface EnableBlossomCache {

}
