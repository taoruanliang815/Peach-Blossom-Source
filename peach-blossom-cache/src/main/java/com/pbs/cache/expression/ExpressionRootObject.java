package com.pbs.cache.expression;

import lombok.Data;

/**
 * @author taoruanliang
 * @date 2022/4/28 11:35
 */
@Data
public class ExpressionRootObject {

    private final Object object;
    private final Object[] args;

    public ExpressionRootObject(Object object, Object[] args) {
        this.object = object;
        this.args = args;
    }
}
