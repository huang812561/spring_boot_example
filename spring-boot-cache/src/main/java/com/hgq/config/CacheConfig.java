package com.hgq.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.hgq.constants.CacheEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.hgq.constants.CacheConstants.ENUM_CACHE_MANAGER;

/**
 * 配置缓存
 *
 * @Author hgq
 * @Date: 2022-03-28 14:57
 * @since 1.0
 **/
@EnableCaching
@Configuration
@Slf4j
public class CacheConfig {


    @Primary
    @Bean(name = "cacheManager")
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        Caffeine<Object, Object> caffeine = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.SECONDS)////最后一次写入后经过固定时间过期
                .maximumSize(500);//缓存的最大条数
        cacheManager.setCaffeine(caffeine);
        return cacheManager;
    }

    @Bean(name = "cacheSysManager")
    public CacheManager cacheSysManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        Caffeine<Object, Object> caffeine = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.SECONDS)////最后一次写入后经过固定时间过期
                .maximumSize(500);//缓存的最大条数
        cacheManager.setCaffeine(caffeine);
        return cacheManager;

    }

    /**
     * Caffeine配置说明：
     * initialCapacity=[integer]: 初始的缓存空间大小
     * maximumSize=[long]: 缓存的最大条数
     * maximumWeight=[long]: 缓存的最大权重
     * expireAfterAccess=[duration]: 最后一次写入或访问后经过固定时间过期
     * expireAfterWrite=[duration]: 最后一次写入后经过固定时间过期
     * refreshAfterWrite=[duration]: 创建缓存或者最近一次更新缓存后经过固定的时间间隔，刷新缓存
     * weakKeys: 打开key的弱引用
     * weakValues：打开value的弱引用
     * softValues：打开value的软引用
     * recordStats：开发统计功能l
     * 注意：
     * expireAfterWrite和expireAfterAccess同事存在时，以expireAfterWrite为准。
     * maximumSize和maximumWeight不可以同时使用
     * weakValues和softValues不可以同时使用
     */
    @Bean(name = ENUM_CACHE_MANAGER)
    public CacheManager enumCacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        List<CaffeineCache> list = new ArrayList<>();
        /**
         * 循环添加枚举类中自定义的缓存，可以自定义
         */
        for (CacheEnum cacheEnum : CacheEnum.values()) {
            list.add(new CaffeineCache(cacheEnum.getName(),

                    Caffeine.newBuilder()
                            .initialCapacity(cacheEnum.getInitialCapacity())
                            .maximumSize(cacheEnum.getMaximum())
                            .expireAfterAccess(cacheEnum.getExpires(), TimeUnit.SECONDS)
                            .build()));
        }
        cacheManager.setCaches(list);
        return cacheManager;
    }

}
