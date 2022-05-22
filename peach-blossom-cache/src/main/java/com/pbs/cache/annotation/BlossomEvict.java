package com.pbs.cache.annotation;

import java.lang.annotation.*;

/**
 * 清除缓存注解
 *
 * @author taoruanliang
 * @date 2022/2/16 13:49
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface BlossomEvict {

    String key();

}
