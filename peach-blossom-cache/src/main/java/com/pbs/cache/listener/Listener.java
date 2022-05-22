package com.pbs.cache.listener;

import com.pbs.cache.event.Event;

/**
 * @author taoruanliang
 * @date 2022/4/22 15:31
 */
public interface Listener {

    void onEvent(Event event);
}
