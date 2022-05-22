package com.pbs.cache.listener;

import com.pbs.cache.annotation.BlossomSubscribe;
import com.pbs.cache.event.CacheEvent;
import com.pbs.cache.event.Event;
import com.pbs.cache.manager.BlossomCacheManager;
import lombok.extern.slf4j.Slf4j;

/**
 * @author taoruanliang
 * @date 2022/4/22 16:01
 */
@Slf4j
@BlossomSubscribe(eventType = CacheEvent.class)
public class CacheListener implements Listener {

    @Override
    public void onEvent(Event event) {
        CacheEvent cacheEvent = (CacheEvent) event;
        BlossomCacheManager.put(cacheEvent.getCacheKey(), cacheEvent.getResult());
    }
}
