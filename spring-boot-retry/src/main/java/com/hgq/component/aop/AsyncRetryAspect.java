package com.hgq.component.aop;

import com.hgq.component.ann.AsyncRetry;
import com.hgq.component.ann.Retry;
import com.hgq.entity.RetryInvorkParam;
import com.hgq.service.RetryScheduledEngine;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
public class AsyncRetryAspect {
    @Autowired
    private RetryScheduledEngine scheduledEngine;

    @Pointcut(value = "@annotation(com.hgq.component.ann.AsyncRetry)")
    public void executeRetry() {
    }

    /**
     * 异步重试策略
     */
    @Around("executeRetry()")
    public Object doAround2(ProceedingJoinPoint joinPoint){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Object[] args = joinPoint.getArgs();
        Object target = joinPoint.getTarget();

        AsyncRetry retry = method.getAnnotation(AsyncRetry.class);
        RetryInvorkParam retryParam = RetryInvorkParam.builder()
                .invorkMethod(method)
                .target(target)
                .args(args)
                .currentTimes(0)
                .retryTimes(retry.maxAttempts())
                .delayed(retry.waitTime())
                .isEnd(false)
                .needThrowExceptions(retry.needThrowExceptions())
                .build();
        scheduledEngine.submit(retryParam);

        return false;
    }


}
