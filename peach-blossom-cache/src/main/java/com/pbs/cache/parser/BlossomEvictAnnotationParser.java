package com.pbs.cache.parser;


import com.pbs.cache.annotation.BlossomEvict;
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
public class BlossomEvictAnnotationParser {

    @Autowired
    private BlossomCacheRocketMqSender blossomCacheRocketMqSender;

    public Object parse(BlossomEvict blossomEvict, ProceedingJoinPoint joinPoint, Object[] args) throws Throwable {

        if (blossomEvict == null) {
            return joinPoint.proceed(args);
        }

        String cacheKey = ExpressionParser.parseKey(joinPoint, blossomEvict.cacheKey());

        return doEvict(blossomEvict, cacheKey, joinPoint, args);
    }

    private Object doEvict(BlossomEvict blossomEvict, String cacheKey, ProceedingJoinPoint joinPoint, Object[] args) throws Throwable {
        String businessKey = blossomEvict.businessKey();
        BlossomCacheManager.getLocalCache(businessKey).remove(cacheKey);
        if (blossomEvict.isRemote()) {
            syncEvict(businessKey, cacheKey);
        }
        return joinPoint.proceed(args);
    }

    private void syncEvict(String businessKey, String cacheKey) {
        BlossomCacheMessage message = new BlossomCacheMessage();
        message.setType(BlossomCacheSyncTypeEnum.EVICT.getType());
        message.setCacheKey(cacheKey);
        message.setBusinessKey(businessKey);
        blossomCacheRocketMqSender.syncSend(message);
    }
}
