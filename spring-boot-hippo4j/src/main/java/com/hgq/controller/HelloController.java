package com.hgq.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * todo
 *
 * @Author hgq
 * @Date: 2022-08-17 20:30
 * @since 1.0
 **/
@RestController
public class HelloController {

    @Resource
    private ThreadPoolExecutor messageConsumeDynamicExecutor;

    /*@Resource
    private ThreadPoolExecutor messageProduceDynamicExecutor;*/

    @RequestMapping("test")
    public String test() {
        messageConsumeDynamicExecutor.execute(() -> {
            System.out.println("messageConsumeDynamicExecutor 当前线程正在执行：" + Thread.currentThread().getName());
        });
        return "success";
    }

    @RequestMapping("testConsume")
    public String testConsume() {
        messageConsumeDynamicExecutor.execute(() -> {
            System.out.println("messageConsumeDynamicExecutor  当前线程正在执行：" + Thread.currentThread().getName());
        });
        return "success";
    }


    @RequestMapping("testProduce")
    public String testProduce() {
        messageConsumeDynamicExecutor.execute(() -> {
            System.out.println("messageConsumeDynamicExecutor 当前线程正在执行：" + Thread.currentThread().getName());
        });
        return "success";
    }


}
