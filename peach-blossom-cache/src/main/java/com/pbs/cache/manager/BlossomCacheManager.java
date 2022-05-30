package com.pbs.cache.manager;


import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;


/**
 * @author taoruanliang
 * @date 2022/3/10 17:22
 */
public class BlossomCacheManager {

    private static final ConcurrentMap<String, BlossomCache> caches = new ConcurrentHashMap<>();

    public static synchronized BlossomCache getLocalCache(String businessKey) {

        if (caches.containsKey(businessKey)) {
            return caches.get(businessKey);
        } else {
            LocalCache cache = new LocalCache();
            caches.put(businessKey, cache);
        }

        return caches.get(businessKey);
    }

    public interface BlossomCache {

        Object get(String key);

        void put(String key, Object value);

        void remove(String key);
    }

    public static class LocalCache implements BlossomCache {

        private static final Cache<String, Object> cache = CacheBuilder
                .newBuilder()
                .maximumSize(1000)
                .expireAfterAccess(10, TimeUnit.MINUTES).build();

        @Override
        public Object get(String key) {
            return cache.getIfPresent(key);
        }

        @Override
        public void put(String key, Object value) {
            cache.put(key, value);
        }

        @Override
        public void remove(String key) {
            cache.invalidate(key);
        }
    }
}
