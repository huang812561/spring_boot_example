package com.hgq.config;

import cn.hippo4j.core.executor.DynamicThreadPool;
import cn.hippo4j.core.executor.support.ThreadPoolBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 通过 ThreadPoolBuilder 构建动态线程池，
 * 只有 threadFactory、threadPoolId 为必填项，其它参数会从配置中心拉取。
 *
 * @Author hgq
 * @Date: 2022-08-17 20:11
 * @since 1.0
 **/
@Configuration
public class ThreadPoolConfig {

/*    @Bean
    @DynamicThreadPool
    public ThreadPoolExecutor demoConsumeDynamicExecutor() {
        String threadPoolId = "message-consume2";
        ThreadPoolExecutor messageConsumeDynamicExecutor = ThreadPoolBuilder.builder()
                .threadFactory(threadPoolId)
                .threadPoolId(threadPoolId)
                .dynamicPool()
                .build();
        return messageConsumeDynamicExecutor;
    }*/

    @Bean
    @DynamicThreadPool
    public ThreadPoolExecutor messageConsumeDynamicExecutor() {
        String threadPoolId = "message-consume";
        ThreadPoolExecutor messageConsumeDynamicExecutor = ThreadPoolBuilder.builder()
                .threadFactory(threadPoolId)
                .threadPoolId(threadPoolId)
                .dynamicPool()
                .build();
        return messageConsumeDynamicExecutor;
    }

/*    @Bean
    @DynamicThreadPool
    public ThreadPoolExecutor messageProduceDynamicExecutor() {
        String threadPoolId = "message-produce1";
        ThreadPoolExecutor messageProduceDynamicExecutor = ThreadPoolBuilder.builder()
                .threadPoolId(threadPoolId)
                .threadFactory(threadPoolId)
                .dynamicPool()
                .build();

        return messageProduceDynamicExecutor;
    }*/

}
