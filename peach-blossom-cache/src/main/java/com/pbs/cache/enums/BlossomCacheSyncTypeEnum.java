package com.pbs.cache.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author taoruanliang
 * @date 2022/4/28 16:31
 */
@Getter
@AllArgsConstructor
public enum BlossomCacheSyncTypeEnum {

    CACHE(1),

    EVICT(2);

    Integer type;

    public static BlossomCacheSyncTypeEnum getEnum(Integer type) {
        return Arrays.stream(BlossomCacheSyncTypeEnum.values()).filter(e -> e.type == type).findFirst().get();
    }
}
