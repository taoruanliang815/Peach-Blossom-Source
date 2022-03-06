package com.peach.blossom.cache.cache;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author taoruanliang
 * @date 2022/2/21 21:53
 */
@Getter
@AllArgsConstructor
public enum BlossomCacheManagerEnum {

    GUAVA_MANAGER("GUAVA_MANAGER"),

    REDIS_MANAGER("REDIS_MANAGER"),

    LOCAL_MANAGER("LOCAL_MANAGER");

    private String cacheManager;
}
