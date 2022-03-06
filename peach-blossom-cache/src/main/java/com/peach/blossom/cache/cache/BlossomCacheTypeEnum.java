package com.peach.blossom.cache.cache;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author taoruanliang
 * @date 2022/2/17 11:23
 */
@Getter
@AllArgsConstructor
public enum BlossomCacheTypeEnum {

    SENDER(1),

    RECEIVER(2);

    private Integer type;
}
