package com.pbs.cache.event;

import com.pbs.cache.annotation.BlossomSubscribe;
import com.pbs.cache.listener.Listener;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author taoruanliang
 * @date 2022/4/22 15:35
 */
public class Publisher {

    private static ConcurrentHashMap<Class<? extends Event>, List<Listener>> listenerMap = new ConcurrentHashMap<>();

    private synchronized static void addListener(Class<? extends Event> eventType, Listener listener) {
        List<Listener> listeners = listenerMap.get(eventType);
        if (listeners == null) {
            listeners = new ArrayList<>();
            listenerMap.put(eventType, listeners);
        }
        listeners.add(listener);
    }

    public static void publish(Event event) {
        for (Listener listener : listenerMap.get(event.getClass())) {
            listener.onEvent(event);
        }
    }

    public static void subscribe() throws InstantiationException, IllegalAccessException {
        String packageName = Listener.class.getPackage().getName();
        Set<Class<?>> set = new Reflections(packageName).getTypesAnnotatedWith(BlossomSubscribe.class);
        for (Class<?> c : set) {
            BlossomSubscribe subscribe = c.getAnnotation(BlossomSubscribe.class);
            addListener(subscribe.eventType(), (Listener) c.newInstance());
        }
    }
}
