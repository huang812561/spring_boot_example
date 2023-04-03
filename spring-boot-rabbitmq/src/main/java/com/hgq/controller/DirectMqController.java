package com.hgq.controller;

import com.hgq.service.PublisherApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 1. 直连型交换机（Direct Exchange）
 *      据消息携带的路由键routingKey将消息投递给对应队列。
 *
 * @Author hgq
 * @Date: 2022-07-07 17:25
 * @since 1.0
 **/
@RestController
public class DirectMqController {

    @Autowired
    private PublisherApi publisherApi;

    @RequestMapping("/direct/{routingKey}/{message}/{count}")
    public String direct(@PathVariable("routingKey") String routingKey, @PathVariable("message") String message, @PathVariable("count") int count) {
        for (int i = 1; i <= count; i++) {
            publisherApi.sendDirectMail(routingKey, message);
        }
        System.out.println("########### direct 消息发送成功##################");
        return "消息发送成功";
    }
}
