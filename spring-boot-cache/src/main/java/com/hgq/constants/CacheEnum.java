package com.hgq.constants;

import lombok.Getter;

/**
 * @description: 缓存配置
 * @author: hgq
 * @time: 2023/3/21 19:26
 */
@Getter
public enum CacheEnum {

    OPERATOR_CACHE("cache:operator", 5000L, 50, 1000),
    USER_CACHE("cache:user", 10000L, 50, 1000),
    ORG_CACHE("cache:org", 2000L, 50, 1000);

    CacheEnum(String name, long expires, int initialCapacity, long maximum) {
        this.name = name;
        this.expires = expires;
        this.maximum = maximum;
        this.initialCapacity = initialCapacity;
    }

    private String name;
    private long expires;
    private int initialCapacity;
    private long maximum;

}
