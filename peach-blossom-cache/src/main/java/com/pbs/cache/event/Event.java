package com.pbs.cache.event;


import com.pbs.cache.enums.BlossomCacheSyncTypeEnum;
import com.pbs.cache.mq.BlossomCacheMessage;

/**
 * @author taoruanliang
 * @date 2022/4/22 17:22
 */
public class Event {

    public static Event event(BlossomCacheMessage message) {

        switch (BlossomCacheSyncTypeEnum.getEnum(message.getType())) {
            case CACHE:
                return new CacheEvent(message.getBusinessKey(), message.getCacheKey(), message.getResult());
            case EVICT:
                return new EvictEvent(message.getBusinessKey(), message.getCacheKey());
            default:
                return null;
        }
    }
}
