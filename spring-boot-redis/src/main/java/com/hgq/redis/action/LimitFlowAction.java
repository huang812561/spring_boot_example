package com.hgq.redis.action;

import com.hgq.redis.component.annotation.RedisCountLimit;
import com.hgq.redis.component.annotation.RedisTokenBucketLimit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 限流-测试
 *
 * @Author hgq
 * @Date: 2022-03-25 16:04
 * @since 1.0
 **/
@RestController
@Slf4j
public class LimitFlowAction {


    private static AtomicInteger atoInt = new AtomicInteger(0);

    /**
     * 5秒内hello只能访问5次
     *
     * @return
     */
    @RedisCountLimit(name = "hello", key = "hello", prefix = "hello", count = 5, period = 5)
    @RequestMapping("countLimit")
    public String hello() {
        return "success";
    }


    @RequestMapping("/tokenBucketLimit")
    @RedisTokenBucketLimit(capacity = 2,rate = 1,requested = 1,tokensKey = "redisLimit2")
    public String testRedisLimit2() {
        log.info("当前请求数量为:[{}]", atoInt.incrementAndGet());
        return "success";
    }

}
