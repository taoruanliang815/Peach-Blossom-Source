package com.pbs.cache.parser;


import com.pbs.cache.annotation.BlossomCache;
import com.pbs.cache.enums.BlossomCacheSyncTypeEnum;
import com.pbs.cache.expression.ExpressionParser;
import com.pbs.cache.manager.BlossomCacheManager;
import com.pbs.cache.mq.BlossomCacheMessage;
import com.pbs.cache.mq.BlossomCacheRocketMqSender;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author taoruanliang
 * @date 2022/2/17 10:43
 */
public class BlossomCacheAnnotationParser {

    @Autowired
    private BlossomCacheRocketMqSender blossomCacheRocketMqSender;

    public Object parse(BlossomCache blossomCache, ProceedingJoinPoint joinPoint, Object[] args) throws Throwable {

        if (blossomCache == null) {
            return joinPoint.proceed(args);
        }

        String cacheKey = ExpressionParser.parseKey(joinPoint, blossomCache.cacheKey());

        return doGetCache(blossomCache, cacheKey, joinPoint, args);
    }

    private Object doGetCache(BlossomCache blossomCache, String cacheKey, ProceedingJoinPoint joinPoint, Object[] args) throws Throwable {
        String businessKey = blossomCache.businessKey();
        Object result = BlossomCacheManager.getLocalCache(businessKey).get(cacheKey);
        if (result == null) {
            result = joinPoint.proceed(args);
            BlossomCacheManager.getLocalCache(businessKey).put(cacheKey, result);
            if (blossomCache.isRemote()) {
                syncCache(businessKey, cacheKey, result);
            }
        }
        return result;
    }

    private Object syncCache(String businessKey, String cacheKey, Object result) {
        BlossomCacheMessage message = new BlossomCacheMessage();
        message.setType(BlossomCacheSyncTypeEnum.CACHE.getType());
        message.setCacheKey(cacheKey);
        message.setResult(result);
        message.setBusinessKey(businessKey);
        blossomCacheRocketMqSender.syncSend(message);
        return result;
    }
}
