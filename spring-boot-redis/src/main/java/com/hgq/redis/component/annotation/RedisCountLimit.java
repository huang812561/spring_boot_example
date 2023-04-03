package com.hgq.redis.component.annotation;

import java.lang.annotation.*;

/**
 * 计数器-限流
 *
 * @Author hgq
 * @Date: 2022-03-25 16:05
 * @since 1.0
 **/
@Documented
@Target(value = {ElementType.METHOD, ElementType.TYPE})
@Retention(value = RetentionPolicy.RUNTIME)
@Inherited
public @interface RedisCountLimit {


    /**
     * 资源名称
     */
    String name() default "";

    /**
     * 资源的key
     * @return
     */
    String key() default "";

    /**
     * 资源的前缀
     * @return
     */
    String prefix() default "";

    /**
     * 最多访问限制次数
     */
    int count();

    /**
     * 给定时间段
     */
    int period();

    /**
     * 限流类型
     */
    LimitType limitType() default LimitType.CUSTOMER;

    enum LimitType {

        /**
         * 自定义key
         */
        CUSTOMER,

        /**
         * 根据IP
         */
        IP;
    }




}
