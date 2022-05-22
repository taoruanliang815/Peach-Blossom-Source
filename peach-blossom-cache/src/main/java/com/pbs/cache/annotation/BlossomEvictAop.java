package com.pbs.cache.annotation;

import com.pbs.cache.parser.BlossomEvictAnnotationParser;
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
public class BlossomEvictAop {

    @Autowired
    private BlossomEvictAnnotationParser blossomEvictAnnotationParser;

    @Around("@annotation(com.pbs.cache.annotation.BlossomEvict)")
    public Object process(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Method joinPointMethod = ((MethodSignature) joinPoint.getSignature()).getMethod();
        BlossomEvict blossomEvict = joinPointMethod.getAnnotation(BlossomEvict.class);
        Object result = blossomEvictAnnotationParser.parse(blossomEvict, joinPoint, args);
        return result;
    }
}
