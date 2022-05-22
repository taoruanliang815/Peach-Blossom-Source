package com.pbs.cache.expression;

import org.springframework.aop.support.AopUtils;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.context.expression.CachedExpressionEvaluator;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author taoruanliang
 * @date 2022/4/28 11:36
 */
public class ExpressionEvaluator<T> extends CachedExpressionEvaluator {
    private final ParameterNameDiscoverer paramNameDiscoverer = new DefaultParameterNameDiscoverer();
    private final Map conditionCache = new ConcurrentHashMap<>(64);
    private final Map<AnnotatedElementKey, Method> targetMethodCache = new ConcurrentHashMap<>(64);


    public EvaluationContext createEvaluationContext(Object object, Class targetClass, Method method, Object[] args) {
        Method targetMethod = getTargetMethod(targetClass, method);
        ExpressionRootObject root = new ExpressionRootObject(object, args);
        return new MethodBasedEvaluationContext(root, targetMethod, args, paramNameDiscoverer);
    }


    public T condition(String conditionExpression, AnnotatedElementKey elementKey, EvaluationContext evalContext, Class clazz) {
        return (T) getExpression(this.conditionCache, elementKey, conditionExpression).getValue(evalContext, clazz);
    }

    private Method getTargetMethod(Class targetClass, Method method) {
        AnnotatedElementKey methodKey = new AnnotatedElementKey(method, targetClass);
        Method targetMethod = targetMethodCache.get(methodKey);
        if (targetMethod == null) {
            targetMethod = AopUtils.getMostSpecificMethod(method, targetClass);
            if (targetMethod == null) {
                targetMethod = method;
            }
            targetMethodCache.put(methodKey, targetMethod);
        }
        return targetMethod;
    }
}
