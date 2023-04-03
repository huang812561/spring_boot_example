package com.hgq.util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 继承ThreadLocal-初始化初始值
 *
 * @Author hgq
 * @Date: 2022-05-24 13:53
 * @since 1.0
 **/
public class MyThreadLocal extends ThreadLocal<AtomicInteger>{
    @Override
    protected AtomicInteger initialValue() {
        return new AtomicInteger(0);
    }
}
