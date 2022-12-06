package com.pbs.common.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author taoruanliang
 * @date 2022/2/17 10:43
 */
public class DistinctUtil {


    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {

        Map seen = new ConcurrentHashMap<>();

        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}
