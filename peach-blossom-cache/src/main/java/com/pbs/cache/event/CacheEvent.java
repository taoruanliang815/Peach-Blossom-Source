package com.pbs.cache.event;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author taoruanliang
 * @date 2022/4/29 13:24
 */

@Data
@AllArgsConstructor
public class CacheEvent extends Event {

    private String businessKey;

    private String cacheKey;

    private Object result;

}
