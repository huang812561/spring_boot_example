package com.hgq.controller;

import com.hgq.service.ProducerApi;
import com.hgq.service.PublisherApi;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

/**
 * 未封装接口，直接调用 RabbitTemplate
 *
 * @Author hgq
 * @Date: 2022-07-07 10:22
 * @since 1.0
 **/
@RestController
public class HelloController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RequestMapping("/sendQueue/{message}/{count}")
    public String sendQueue(@PathVariable("message") String message, @PathVariable("count") int count) {
        for (int i = 1; i <= count; i++) {
            rabbitTemplate.send("orderQueue", new Message(message.getBytes(StandardCharsets.UTF_8)));
            rabbitTemplate.convertAndSend("orderQueue", "发送第 " + i + " 条MQ 消息内容:" + message);
            rabbitTemplate.send("orderEchange", "order", new Message((message).getBytes(StandardCharsets.UTF_8)));
            rabbitTemplate.convertAndSend("orderEchange", "orderQueue", "发送第 " + i + " 条MQ 消息内容:" + message);
            rabbitTemplate.send("orderEchange", "orderQueue", new Message((message).getBytes(StandardCharsets.UTF_8)),new CorrelationData());
        }
        System.out.println("###########消息发送成功##################");
        return "消息发送成功";
    }



}
