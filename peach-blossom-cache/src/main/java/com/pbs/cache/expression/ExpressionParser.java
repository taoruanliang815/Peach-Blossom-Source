package com.pbs.cache.expression;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.expression.EvaluationContext;

/**
 * @author taoruanliang
 * @date 2022/4/28 14:22
 */
public class ExpressionParser {

    private static final ExpressionEvaluator<String> expressionEvaluator = new ExpressionEvaluator();

    public static String parseKey(ProceedingJoinPoint joinPoint, String expression) {

        AnnotatedElementKey methodKey = new AnnotatedElementKey(
                ((MethodSignature) joinPoint.getSignature()).getMethod(),
                joinPoint.getTarget().getClass());

        EvaluationContext evaluationContext = expressionEvaluator.createEvaluationContext(
                joinPoint.getTarget(),
                joinPoint.getTarget().getClass(),
                ((MethodSignature) joinPoint.getSignature()).getMethod(),
                joinPoint.getArgs());

        return expressionEvaluator.condition(expression, methodKey, evaluationContext, String.class);
    }
}
