package com.hgq.utils;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.Weigher;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @description: Caffeine缓存过期策略
 * @author: hgq
 * @time: 2023/3/20 10:14
 */
public class CaffeineExpiresApiExample {

    public static void main(String[] args) {

        //maximumSizeTest();
        // maximumWeightTest();
        // expireAfterAccessTest();
        refreshAfterWriteTest();
        //recordStatsTest();

    }

    /**
     * 统计
     */
    public static void recordStatsTest() {
        LoadingCache<Object, Object> loadingCache = Caffeine.newBuilder()
                .refreshAfterWrite(1, TimeUnit.SECONDS)
                .expireAfterAccess(1, TimeUnit.SECONDS)
                .expireAfterWrite(1, TimeUnit.SECONDS)
                .maximumSize(10)
                //开启记录缓存命中率等信息
                .recordStats()
                .build(key -> initKey(key));
        loadingCache.put("1", "张三");
        loadingCache.get("1");
        loadingCache.get("2");

        /**
         * 命中次数：hitCount=1,
         * 未命中次数：missCount=0,
         * 成功加载新值的次数：loadSuccessCount=0,
         * 失败加载新值的次数：loadFailureCount=0,
         * 全部的加载时间：totalLoadTime=0, 总纳秒数
         * 丢失的条数：evictionCount=0,
         * 丢失的比重：evictionWeight=0
         *
         */
        System.out.println("缓存命中率：" + loadingCache.stats());
    }

    /**
     * refreshAfterWrite仅支持
     * LoadingCache
     * AsyncLoadingCache
     * <p>
     * LoadingCache 自动加载值
     * 刷新机制
     */
    @SneakyThrows
    public static void refreshAfterWriteTest() {

        LoadingCache<Object, Object> loadingCache = Caffeine.newBuilder()
                .refreshAfterWrite(1, TimeUnit.SECONDS)
                .build(key -> initKey(key));

        //获取ID=1的值，由于缓存里还没有，所以会自动放入缓存
        System.out.println(loadingCache.get("1"));
        Thread.sleep(2000L);
        //触发刷新的请求将异步调用 CacheLoader.reload 并立即返回旧值。注意这里返回上一次的最新值，并没有+1
        System.out.println(loadingCache.get("1"));
        System.out.println(loadingCache.get("1"));
        System.out.println(loadingCache.get("2"));
        System.out.println(loadingCache.get("3"));
        Map<Object, Object> allPresent = loadingCache.getAllPresent(Arrays.asList("1", "2", "3"));
        System.out.println("批量获取：" + allPresent.toString());
    }

    public static int num = 0;

    /**
     * 初始化key对应的value
     *
     * @param key
     * @return
     */
    private static Object initKey(Object key) {
        return "key:" + key + ", num:" + (++num);
    }

    /**
     * expireAfterAccess: 访问后到期（每次访问都会重置访问时间，如果一直被访问就不会被淘汰）
     * expireAfterWrite :写入后到期
     */
    @SneakyThrows
    public static void expireAfterAccessTest() {
        Cache<Object, Object> cache = Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.SECONDS)
                //.expireAfterAccess(1, TimeUnit.SECONDS)
                //.expireAfter(new Expiry<Object, Object>() {
                //
                //    /**
                //     *
                //     * 1 毫秒= 1000000 纳秒
                //     * @param key
                //     * @param value
                //     * @param currentTime 单位为：纳秒
                //     * @return
                //     */
                //    @Override
                //    public long expireAfterCreate(Object key, Object value, long currentTime) {
                //        System.out.println("key:"+key+",val:"+value);
                //        return 1000000000L;
                //    }
                //
                //    @Override
                //    public long expireAfterUpdate(Object key, Object value, long currentTime, long currentDuration) {
                //        return 1000000000L;
                //    }
                //
                //    @Override
                //    public long expireAfterRead(Object key, Object value, long currentTime, long currentDuration) {
                //        return 1000000000L;
                //    }
                //})
                .removalListener((key, value, cause) -> {
                    System.out.println("淘汰缓存：key:" + key + ", value:" + value);
                })
                .build();

        cache.put("1", "1");
        System.out.println(cache.getIfPresent("1"));
        Thread.sleep(2000L);
        System.out.println(cache.getIfPresent("1"));
        System.out.println("批量获取：" + cache.getAllPresent(Arrays.asList("1", "2", "3")));
    }


    /**
     * 权重淘汰
     */
    @SneakyThrows
    public static void maximumWeightTest() {
        Cache<Integer, Object> cache = Caffeine.newBuilder()
                //限制总权重，若所有缓存的权重加起来 > 总权重就会淘汰权重小的缓存
                .maximumWeight(100)
                .weigher((Weigher<Integer, Object>) (key, value) -> key)
                .removalListener((key, value, cause) -> {
                    System.out.println("淘汰缓存：key:" + key + ", value:" + value);
                })
                .build();

        int maximumWeight = 0;
        for (int i = 0; i < 20; i++) {
            cache.put(i, i);
            maximumWeight += i;
        }
        System.out.println("总权重" + maximumWeight);
        Thread.sleep(1000L);
        System.out.println("缓存的值:" + cache.asMap());
    }

    /**
     * 大小（LFU 淘汰策略，一段时间内使用次数最少的）
     */
    @SneakyThrows
    public static void maximumSizeTest() {
        Cache<Integer, Object> cache = Caffeine.newBuilder()
                .maximumSize(10)
                .removalListener((key, value, cause) -> {
                    System.out.println("淘汰缓存：key:" + key + ", value:" + value);
                })
                .build();

        for (int i = 0; i < 20; i++) {
            cache.put(i, "key" + i);
        }
        /**
         * 缓存淘汰是异步处理的
         */
        Thread.sleep(500L);
        /**
         * 打印没有被淘汰的值
         */
        System.out.println("缓存的值：" + cache.asMap());
        List<Integer> keys = new ArrayList<>();
        keys.add(17);
        keys.add(18);
        keys.add(19);
        System.out.println("批量获取："+cache.getAllPresent(keys));
    }


}
