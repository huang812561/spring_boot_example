package com.hgq.util;

import com.github.rholder.retry.*;
import com.google.common.base.Predicates;
import com.hgq.component.ann.Retry;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * Guava-retrying工具类
 *
 * @Author hgq
 * @Date: 2022-05-24 19:50
 * @since 1.0
 **/
public class RetryUtil {

    /**
     * 默认三次
     */
    public static Retryer<Boolean> retryer = RetryerBuilder.<Boolean>newBuilder()
            .retryIfResult(Predicates.<Boolean>isNull())//1. 当重试的方法返回Null时进行重试
            .retryIfExceptionOfType(Exception.class)    //2. 当重试方法抛出exception时进行重试
            .withStopStrategy(StopStrategies.stopAfterAttempt(3)) //3. 尝试执行三次（1+2）
            .withWaitStrategy(WaitStrategies.fixedWait(1L, TimeUnit.SECONDS)) //4.重试间隔时长
            .build();

    /**
     * @return
     */
    public static Retryer<Boolean> getRetryer(int retryTimes,long sleepTime) {
        retryer = RetryerBuilder.<Boolean>newBuilder()
                .retryIfResult(Predicates.<Boolean>isNull())//1. 当重试的方法返回Null时进行重试
                .retryIfException()//2. 当重试方法抛出exception时进行重试
                .retryIfExceptionOfType(IllegalAccessException.class)    //2. 当重试方法抛出IllegalAccessException时进行重试
                .withStopStrategy(StopStrategies.stopAfterAttempt(retryTimes)) //3. 尝试执行三次（1+2）
                .withWaitStrategy(WaitStrategies.fixedWait(sleepTime, TimeUnit.SECONDS)) //4.重试间隔时长
                .build();
        return retryer;
    }

    public static void main(String[] args) {
        Callable<Boolean> callable = new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return doSomething();
            }
        };

        try {
            RetryUtil.getRetryer(3,3).call(callable);
        }catch (Exception e ){
            e.printStackTrace();
        }
        System.out.println("执行完");
    }

    private static boolean doSomething() {
        randomEx();
        return true;
    }

    private static void randomEx() {
        int random = ThreadLocalRandom.current().nextInt(100);
        if (random % 10 > 1) {
            throw new RuntimeException("随机异常");
        }
    }

}
