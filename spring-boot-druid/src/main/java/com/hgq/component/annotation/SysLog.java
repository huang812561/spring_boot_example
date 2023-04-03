package com.hgq.component.annotation;

import java.lang.annotation.*;

/**
 * 输出日志注解
 *
 * @Author hgq
 * @Date: 2022-04-01 13:59
 * @since 1.0
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface SysLog {

    /**
     * 系统名称
     */
    String service() default "log_server";

    /**
     * 模块描述
     *
     * @return
     */
    String moudle() default "";

    /**
     * 方法名
     *
     * @return
     */
    String method() default "";

    /**
     * 描述
     *
     * @return
     */
    String desc() default "";
}
