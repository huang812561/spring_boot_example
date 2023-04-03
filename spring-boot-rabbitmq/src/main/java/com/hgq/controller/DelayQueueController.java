package com.hgq.controller;

import com.hgq.rabbitmq.queue.DelayQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * todo
 *
 * @Author hgq
 * @Date: 2022-08-30 19:01
 * @since 1.0
 **/
@RestController
public class DelayQueueController {

    @Autowired
    private DelayQueue delayQueue;

    /**
     * 发送延迟队列
     */
    @RequestMapping("/sendDelayMsg")
    public void sendDelayMsg(){
        delayQueue.sendDelayQueue("delayTest","这是延时消息",5000);
    }
}
