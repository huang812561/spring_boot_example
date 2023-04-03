package com.hgq.component.ann;

import java.lang.annotation.*;

/**
 * 自定义重试注解-同步
 *
 * @Author hgq
 * @Date: 2022-05-24 13:59
 * @since 1.0
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Retry {
    /**
     * 最大重试次数
     */
    int maxAttempts() default 3;

    /**
     * 重试的时间间隔
     *
     * @return
     */
    int waitTime() default 1000;

    /**
     34      * 需要抛出的异常, 这些异常发生时, 将直接报错, 不再重试。
     35      * 传入一些异常的class对象
     36      * 如UserException.class
     37      * 当数组长度为0时, 那么都不会抛出, 会继续重试
     38      *
     39      * @return 异常数组
     40      */
    Class[] needThrowExceptions() default {};

    /**
      * 需要捕获的异常, 如果需要捕获则捕获重试。否则抛出异常
      * 执行顺序 needThrowExceptions --> catchExceptions 两者并不兼容
      * 当 needThrowExceptions 判断需要抛出异常时, 抛出异常, 否则进入此方法, 异常不在此数组内则抛出异常
      * 当数组长度为0时, 不会执行捕获异常的逻辑。
      *
      * @return 异常数组
      */
    Class[] catchExceptions() default {};

}
