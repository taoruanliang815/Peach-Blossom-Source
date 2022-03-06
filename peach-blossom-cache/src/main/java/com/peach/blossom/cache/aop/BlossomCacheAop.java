package com.peach.blossom.cache.aop;

import com.peach.blossom.cache.cache.BlossomCache;
import com.peach.blossom.cache.parser.BlossomCacheAnnotationParser;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;

/**
 * @author taoruanliang
 * @date 2021/12/16 10:56
 */
@Aspect
public class BlossomCacheAop {

    @Autowired
    private BlossomCacheAnnotationParser blossomCacheAnnotationParser;

    @Around("@annotation(com.peach.blossom.cache.cache.BlossomCache)")
    public Object process(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Method joinPointMethod = ((MethodSignature) joinPoint.getSignature()).getMethod();
        String[] parameterNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        BlossomCache blossomCache = joinPointMethod.getAnnotation(BlossomCache.class);
        Object result = blossomCacheAnnotationParser.parse(blossomCache, joinPoint, args, parameterNames);
        return result;
    }
}
