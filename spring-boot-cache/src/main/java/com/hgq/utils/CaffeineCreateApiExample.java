package com.hgq.utils;

import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import lombok.SneakyThrows;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @description: Caffeine测试类
 * @author: hgq
 * @time: 2023/3/17 9:42
 */
public class CaffeineCreateApiExample {
    @SneakyThrows
    public static void main(String[] args) {

        /**
         * 1.手动创建缓存cache
         */
        handCacheCreate();

        /**
         * 2.LoadingCache 自动加载的缓存，是附加在CacheLoader之上构建的缓存对象。
         */
        loadCacheCreate();

        /**
         * 3.AsyncLoadingCache
         */
        asyncCacheCreate();

    }

    @SneakyThrows
    private static void asyncCacheCreate() {
        AsyncLoadingCache asyncLoadingCache = Caffeine.newBuilder()
                .initialCapacity(10)
                .maximumSize(100)
                .expireAfterAccess(1, TimeUnit.SECONDS)
                .expireAfterWrite(1, TimeUnit.SECONDS)
                // 到数据库中根据key查询值
                .buildAsync(key -> {
                    Thread.sleep(100);
                    //todo do something
                    return initKey(key);
                });

        //asyncLoadingCache  异步缓存的返回结果是CompletableFuture
        CompletableFuture<String> future = asyncLoadingCache.get("1");
        CompletableFuture future1 = asyncLoadingCache.get("2", key -> initKey("2"));
        CompletableFuture all = asyncLoadingCache.getAll(Arrays.asList("1", "2", "3"));
        System.out.println("获取返回值：" + future.get());
        System.out.println("获取缓存值，存在返回，不存在则初始化：" + future1.get(3L, TimeUnit.SECONDS));
        System.out.println("获取全部缓存值：" + all.get());
    }

    private static void loadCacheCreate() throws InterruptedException {
        /**
         * expireAfterAccess: 当缓存项在指定的时间段内没有被读或写就会被回收。
         * expireAfterWrite：当缓存项在指定的时间段内没有更新就会被回收（移除key），需要等待获取新值才会返回。
         * refreshAfterWrite：当缓存项上一次更新操作之后的多久会被刷新。
         */
        LoadingCache loadingCache = Caffeine.newBuilder()
                .initialCapacity(10)
                .maximumSize(100)
                .expireAfterAccess(1, TimeUnit.SECONDS)
                .expireAfterWrite(3, TimeUnit.SECONDS)
                //创建缓存或者最近一次更新缓存后经过指定时间间隔，刷新缓存；refreshAfterWrite仅支持LoadingCache、AsyncLoadingCache
                .refreshAfterWrite(10, TimeUnit.SECONDS)
                // 到数据库中根据key查询值
                .build(key -> initKey(key));


        //当缓存不存在/缓存已过期时，若调用get()方法，则会自动调用CacheLoader.load()方法加载最新值
        loadingCache.put(1, "aaa");
        System.out.println(loadingCache.get(1));
        loadingCache.put(1, "aaa—new");
        System.out.println(loadingCache.get(2, key -> initKey(key)));
        System.out.println(loadingCache.getIfPresent(3));
        Thread.sleep(5);
        System.out.println("缓存失效后获取到的值：" + loadingCache.get(1));
        Thread.sleep(2);
        System.out.println("缓存失效后获取到的值：" + loadingCache.get(1));
    }

    /**
     * 初始化key 对应的值
     *
     * @return
     */
    private static Object initKey(Object key) {
        return "init_" + key;
    }

    @SneakyThrows
    private static void handCacheCreate() {
        Cache<Object, Object> cache = Caffeine.newBuilder()
                .initialCapacity(100)
                .maximumSize(1000)
                //最后一次写操作后经过指定时间过期
                .expireAfterWrite(1, TimeUnit.SECONDS)
                //最后一次读或写操作后经过指定时间后过期
                .expireAfterAccess(1, TimeUnit.SECONDS)
                //记录命中
                .recordStats()
                //监听缓存被移除
                .removalListener((key, val, removalCause) -> {
                    System.out.println("缓存被移除：" + key + ",值：" + val);
                })
                .build();

        System.out.println(cache.getIfPresent("1"));
        cache.put("1", "张三");
        System.out.println(cache.getIfPresent("1"));
        System.out.println(cache.get("2", o -> "默认值"));
        cache.put("3", "测试移除");
        cache.invalidate("3");
        System.out.println(cache.getIfPresent("3"));

        for (int i = 0; i < 1000; i++) {
            cache.put(i, "张三" + i);
            if (i >= 999) {
                System.out.println(cache.getIfPresent(1));
            }
        }
        CacheStats stats = cache.stats();
        System.out.println(stats.loadCount());
        System.out.println(cache.estimatedSize());
        Thread.sleep(10);
        System.out.println("获取失效后的值" + cache.get("1", o -> initKey("1")));
        Thread.sleep(2);
        System.out.println("获取失效后的值" + cache.getIfPresent("1"));
    }
}
