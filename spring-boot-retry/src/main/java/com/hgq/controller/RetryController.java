package com.hgq.controller;

import com.hgq.component.ann.AsyncRetry;
import com.hgq.component.ann.Retry;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ThreadLocalRandom;

/**
 * todo
 *
 * @Author hgq
 * @Date: 2022-05-24 15:28
 * @since 1.0
 **/
@RestController
public class RetryController {

    /**
     * 测试AOP重试同步处理
     * @return
     */
    @Retry(needThrowExceptions = {RuntimeException.class})
    @RequestMapping("hello")
    public String hello() {
        randomEx();
        return "hello retry";
    }

    /**
     * 测试AOP异步处理
     *
     * @return
     */
    @AsyncRetry(maxAttempts = 10, waitTime = 1, needThrowExceptions = {InvocationTargetException.class})
    @RequestMapping("hello2")
    public boolean hello2() {
        randomEx();
        return true;
    }

    private void randomEx() {
        int random = ThreadLocalRandom.current().nextInt(100);
        if (random % 10 > 5) {
            throw new RuntimeException("随机异常");
        }
    }

}
