package com.hgq.entity;

import lombok.Builder;
import lombok.Data;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * todo
 *
 * @Author hgq
 * @Date: 2022-05-24 17:13
 * @since 1.0
 **/
@Data
@Builder
public class RetryInvorkParam {

    /**
     * 延迟时间
     */
    private int delayed;

    /**
     * 间隔时间
     */
    private long interval;

    /**
     * 重复次数
     */
    private int retryTimes;

    /**
     * 执行的方法的对象
     */
    private Object target;

    /**
     * 执行的参数
     */
    private Object[] args;

    /**
     * 执行的方法
     */
    private Method invorkMethod;

    /**
     * 当前执行的次数
     */
    private volatile long currentTimes;

    /**
     * 下一次执行的时间
     */
    private volatile Date nextInvorkTime;

    /**
     * 是否结束这重试
     */
    private volatile boolean isEnd;

    /**
     *
     */
    private Class[] needThrowExceptions;
}
