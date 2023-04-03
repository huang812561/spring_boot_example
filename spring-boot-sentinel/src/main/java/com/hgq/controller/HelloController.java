package com.hgq.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.hgq.entity.RequestData;
import com.hgq.entity.ResponseEntity;
import com.hgq.util.SentinelExceptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * todo
 *
 * @Author hgq
 * @Date: 2022-08-01 09:24
 * @since 1.0
 **/
@RestController
@Slf4j
public class HelloController {

    @SentinelResource(value = "test", blockHandlerClass = SentinelExceptionUtil.class, blockHandler = "response")
    @RequestMapping("test")
    public ResponseEntity test(@RequestBody RequestData requestData){
        int i = (int) (Math.random() * 10);
        if (i % 2 == 0) {
            throw new RuntimeException("偶数异常");
        }
        return new ResponseEntity();
    }

    @RequestMapping("hello")
    @SentinelResource(value = "hello", blockHandler = "exceptionHandler", fallback = "fallbackTest")
    public String hello() {
        int i = (int) (Math.random() * 10);
        if (i % 2 == 0) {
            throw new RuntimeException("偶数异常");
        }
        return "hello sentinel";
    }

    private String exceptionHandler(BlockException e) {
        log.error("阻塞异常", e);
        return "exception sentinel";
    }

    private String fallbackTest(Throwable throwable) {
        log.error("throwable 异常", throwable);
        return "fallback sentinel";
    }
}
