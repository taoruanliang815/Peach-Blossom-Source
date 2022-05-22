package com.pbs.cache.manager;


import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;


/**
 * @author taoruanliang
 * @date 2022/3/10 17:22
 */
public class BlossomCacheManager {

    private static final Cache<String, Object> cache = CacheBuilder
            .newBuilder()
            .maximumSize(1000)
            .expireAfterAccess(10, TimeUnit.MINUTES).build();

    public static Object get(String key) {
        return cache.getIfPresent(key);
    }

    public static void put(String key, Object value) {
        cache.put(key, value);
    }

    public static void remove(String key) {
        cache.invalidate(key);
    }
}
