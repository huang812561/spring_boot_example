package com.hgq.redis.component.annotation;

import java.lang.annotation.*;

/**
 * 令牌桶-限流
 *
 * @Author hgq
 * @Date: 2022-03-26 09:20
 * @since 1.0
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD, ElementType.TYPE})
@Inherited
public @interface RedisTokenBucketLimit {

    /**
     * 当前限流的标识,可以是ip,或者在spring cloud系统中,可以是一个服务的serviceID
     */
    String tokensKey() default "tokensKey";

    /**
     * 令牌桶刷新的时间戳,后面会被用来计算当前产生的令牌数
     */
    String timestampKey() default "timestampKey";

    /**
     * 令牌生产的速率,如每秒产生10个令牌
     */
    int rate() default 1;

    /**
     * 令牌桶的容积大小,比如最大100个,那么系统最大可承载100个并发请求
     */
    int capacity() default 1;

    /**
     * 当前请求的令牌数量,Spring Cloud Gateway中默认是1,也就是当前请求
     */
    int requested() default 1;

}
