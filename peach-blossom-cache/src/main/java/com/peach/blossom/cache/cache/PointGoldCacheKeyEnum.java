package com.peach.blossom.cache.cache;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author taoruanliang
 * @date 2022/2/21 10:28
 */
@Getter
@AllArgsConstructor
public enum PointGoldCacheKeyEnum {

    APPID_USERNAME("APPID_USERNAME");

    private String key;
}
