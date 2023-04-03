package com.hgq.component.aop;

import com.hgq.component.ann.Retry;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 重试aop
 *
 * @Author hgq
 * @Date: 2022-05-24 14:05
 * @since 1.0
 **/
@Aspect
@Component
@Slf4j
public class RetryAspect {

    @Pointcut(value = "@annotation(com.hgq.component.ann.Retry)")
    public void executeRetry() {
    }

    /**
     * 同步重试策略
     */
    @Around("executeRetry()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws InterruptedException {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Retry retry = method.getAnnotation(Retry.class);

        int maxAttempts = retry.maxAttempts();
        int retryCount = 0;
        Class[] needThrowExceptions = retry.needThrowExceptions();

        while (retryCount < maxAttempts) {
            try {
                return joinPoint.proceed();
            } catch (Throwable e) {
                if (retry.needThrowExceptions().length > 0) {
                    for (Class exClass : needThrowExceptions) {
                        if (e.getClass() == exClass) {
                            retryCount++;
                            Thread.sleep(retry.waitTime());
                            if (retryCount >= maxAttempts) {
                                throw new RuntimeException(e.getMessage());
                            }
                            log.warn("执行重试切面, 第{}次重试, 异常类型:{}, 异常信息:{}", retryCount, e.getClass().getName(), e.getMessage());
                        } else {
                            log.warn("执行重试切面失败, 异常需要在异常抛出的范围{}, 业务抛出的异常类型{}", needThrowExceptions, e.getClass().getName());
                            throw new RuntimeException(e.getMessage());
                        }
                    }
                } else {
                    throw new RuntimeException(e.getMessage());
                }
            }
        }
        return null;
    }

}
