package com.pbs.cache.mq;

import lombok.Data;

/**
 * @author taoruanliang
 * @date 2022/3/10 16:37
 */
@Data
public class BlossomCacheMessage {

    private Integer type;

    private String businessKey;

    private String cacheKey;

    private Object result;
}
