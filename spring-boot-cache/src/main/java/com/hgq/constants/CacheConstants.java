package com.hgq.constants;

/**
 * @description: 缓存常量
 * @author: hgq
 * @time: 2023/3/20 18:21
 */
public class CacheConstants {

    /**
     * 默认过期时间（配置类中我使用的时间单位是秒，所以这里如 3*60 为3分钟）
     */
    public static final int DEFAULT_EXPIRES = 3 * 60;
    public static final int EXPIRES_5_MIN = 5 * 60;
    public static final int EXPIRES_10_MIN = 10 * 60;

    public static final String EMPLOYEE_CACHE_MANAGER = "employeeCache";
    public static final String STS_CACHE_MANAGER = "sysCache";
    public static final String ENUM_CACHE_MANAGER = "enumCacheManager";

}
