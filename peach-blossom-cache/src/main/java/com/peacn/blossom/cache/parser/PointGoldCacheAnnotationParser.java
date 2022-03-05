package com.peacn.blossom.cache.parser;

import com.peach.blossom.cache.cache.PointGoldCache;
import com.peach.blossom.cache.cache.PointGoldCacheTypeEnum;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.ProceedingJoinPoint;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author taoruanliang
 * @date 2022/2/17 10:43
 */
public class PointGoldCacheAnnotationParser {

    private Map cacheMap = new ConcurrentHashMap<>();

    private static final String UNDER_LINE = "_";

    public Object parse(PointGoldCache pointGoldCache, ProceedingJoinPoint joinPoint, Object[] args,
                        String[] parameterNames) throws Throwable {

        if (pointGoldCache == null || ArrayUtils.isEmpty(args) || ArrayUtils.isEmpty(parameterNames)) {
            return joinPoint.proceed(args);
        }

        String[] keyArr = pointGoldCache.key().getKey().split(UNDER_LINE);

        if (!necessary(keyArr, parameterNames)) {
            return joinPoint.proceed(args);
        }

        String cacheKey = parseKey(keyArr, args, parameterNames);

        if (PointGoldCacheTypeEnum.SENDER.compareTo(pointGoldCache.type()) == 0) {
            Object result = joinPoint.proceed(args);
            return send(pointGoldCache.key().getKey(), cacheKey, result);
        }

        if (PointGoldCacheTypeEnum.RECEIVER.compareTo(pointGoldCache.type()) == 0) {
            Object cacheValue = receive(pointGoldCache.key().getKey(), cacheKey, joinPoint, args);
            if (cacheValue != null) {
                return cacheValue;
            }
        }

        return joinPoint.proceed(args);
    }

    private String parseKey(String[] keyArr, Object[] args, String[] parameterNames) {

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i <= keyArr.length - 1; i++) {
            String key = keyArr[i];
            String parameter = Arrays.stream(parameterNames).filter(e -> key.equalsIgnoreCase(e)).findAny().get();
            stringBuilder.append(parameter.toUpperCase());
            stringBuilder.append(UNDER_LINE);
            Object arg = args[Arrays.asList(parameterNames).indexOf(parameter)];
            stringBuilder.append(arg);
            stringBuilder.append(UNDER_LINE);
        }
        return stringBuilder.toString();
    }

    private Boolean necessary(String[] keyArr, String[] parameterNames) {
        for (String parameterName : parameterNames) {
            if (!Arrays.stream(keyArr).filter(e -> e.equalsIgnoreCase(parameterName)).findAny().isPresent()) {
                return false;
            }
        }
        return true;
    }

    private Object send(String key, String cacheKey, Object result) {
        cacheMap.put(cacheKey, result);
        return result;
    }

    private Object receive(String key, String cacheKey, ProceedingJoinPoint joinPoint, Object[] args) throws Throwable {
        Object result = cacheMap.get(cacheKey);
        if (result == null) {
            result = joinPoint.proceed(args);
            cacheMap.put(cacheKey, result);
        }
        return result;
    }
}
