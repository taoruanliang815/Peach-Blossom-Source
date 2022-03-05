package com.peach.blossom.cache.aop;

import com.peach.blossom.cache.cache.PointGoldCache;
import com.peach.blossom.cache.parser.PointGoldCacheAnnotationParser;
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
public class PointGoldCacheAop {

    @Autowired
    private PointGoldCacheAnnotationParser pointGoldCacheAnnotationParser;

    @Around("@annotation(com.peach.blossom.cache.cache.PointGoldCache)")
    public Object process(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Method joinPointMethod = ((MethodSignature) joinPoint.getSignature()).getMethod();
        String[] parameterNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        PointGoldCache pointGoldCache = joinPointMethod.getAnnotation(PointGoldCache.class);
        Object result = pointGoldCacheAnnotationParser.parse(pointGoldCache, joinPoint, args, parameterNames);
        return result;
    }
}
