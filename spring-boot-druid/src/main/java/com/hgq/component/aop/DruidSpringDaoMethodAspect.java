package com.hgq.component.aop;

import com.alibaba.druid.support.spring.stat.DruidStatInterceptor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.JdkRegexpMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Druid 监控
 * druid spring监控配置AOP
 *
 * @Author hgq
 * @Date: 2022-04-02 15:10
 * @since 1.0
 **/
@Configuration
public class DruidSpringDaoMethodAspect {

    @Bean
    public DruidStatInterceptor druidStatInterceptor() {
        return new DruidStatInterceptor();
    }

    /**
     *
     * @return
     */
    @Bean
    public JdkRegexpMethodPointcut druidStatPointcut() {
        JdkRegexpMethodPointcut pointcut = new JdkRegexpMethodPointcut();
        pointcut.setPattern("com.hgq.respository.*");
        return pointcut;
    }

    @Bean
    public DefaultPointcutAdvisor druidAdvisor(){
        DefaultPointcutAdvisor defaultPointcutAdvisor = new DefaultPointcutAdvisor();
        defaultPointcutAdvisor.setPointcut(druidStatPointcut());
        defaultPointcutAdvisor.setAdvice(druidStatInterceptor());
        return defaultPointcutAdvisor;
    }
}
