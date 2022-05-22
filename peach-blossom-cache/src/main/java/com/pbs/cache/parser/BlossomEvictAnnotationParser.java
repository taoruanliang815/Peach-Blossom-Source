package com.pbs.cache.parser;



import com.pbs.cache.annotation.BlossomEvict;
import com.pbs.cache.enums.BlossomCacheSyncTypeEnum;
import com.pbs.cache.expression.ExpressionParser;
import com.pbs.cache.mq.BlossomCacheMessage;
import com.pbs.cache.mq.BlossomCacheRocketMqSender;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author taoruanliang
 * @date 2022/2/17 10:43
 */
public class BlossomEvictAnnotationParser {

    @Autowired
    private BlossomCacheRocketMqSender blossomCacheRocketMqSender;

    private Map<String, Object> cacheMap = new ConcurrentHashMap<>();

    public Object parse(BlossomEvict blossomEvict, ProceedingJoinPoint joinPoint, Object[] args) throws Throwable {

        if (blossomEvict == null) {
            return joinPoint.proceed(args);
        }

        String cacheKey = ExpressionParser.parseKey(joinPoint, blossomEvict.key());

        return doEvict(cacheKey, joinPoint, args);
    }

    private Object doEvict(String cacheKey, ProceedingJoinPoint joinPoint, Object[] args) throws Throwable {
        cacheMap.remove(cacheKey);
        syncEvict(cacheKey);
        return joinPoint.proceed(args);
    }

    private void syncEvict(String cacheKey) {
        BlossomCacheMessage message = new BlossomCacheMessage();
        message.setType(BlossomCacheSyncTypeEnum.EVICT.getType());
        message.setCacheKey(cacheKey);
        blossomCacheRocketMqSender.syncSend(message);
    }
}
