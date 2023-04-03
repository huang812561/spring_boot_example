package com.hgq.service;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.Resource;

import java.util.Objects;

import static com.hgq.constants.CacheConstants.ENUM_CACHE_MANAGER;

/**
 * @description:
 * @author: hgq
 * @time: 2023/3/22 13:17
 */
@Service
public class CaffeineService {


    @Resource(name = ENUM_CACHE_MANAGER)
    private CacheManager cacheManager;


    public String put(String id) {
        Cache cache = cacheManager.getCache("cache:user");
        cache.put(id, "test" + id);
        return String.valueOf(cache.get(id).get());
    }

    public String query(String id) {
        Cache cache = cacheManager.getCache("cache:user");
        Cache.ValueWrapper valueWrapper = cache.get(id);
        if (Objects.isNull(valueWrapper)){
            return "无此值";
        }
        return String.valueOf(valueWrapper.get());
    }

}
