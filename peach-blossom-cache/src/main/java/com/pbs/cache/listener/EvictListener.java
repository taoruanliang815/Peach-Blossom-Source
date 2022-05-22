package com.pbs.cache.listener;

import com.pbs.cache.annotation.BlossomSubscribe;
import com.pbs.cache.event.Event;
import com.pbs.cache.event.EvictEvent;
import com.pbs.cache.manager.BlossomCacheManager;
import lombok.extern.slf4j.Slf4j;

/**
 * @author taoruanliang
 * @date 2022/4/22 16:01
 */

@Slf4j
@BlossomSubscribe(eventType = EvictEvent.class)
public class EvictListener implements Listener {

    @Override
    public void onEvent(Event event) {
        EvictEvent evictEvent = (EvictEvent) event;
        BlossomCacheManager.remove(evictEvent.getCacheKey());
    }
}
