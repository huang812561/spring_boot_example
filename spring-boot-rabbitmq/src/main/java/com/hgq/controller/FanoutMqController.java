package com.hgq.controller;

import com.hgq.service.PublisherApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 2. 扇型交换机 Fanout Exchange
 *      这个交换机没有路由键概念，就算你绑了路由键也是无视的。 这个交换机在接收到消息后，会直接转发到绑定到它上面的所有队列。
 *      绑定的队列
 *      queue1
 *      queue2
 *
 * @Author hgq
 * @Date: 2022-07-07 17:26
 * @since 1.0
 **/
@RestController
public class FanoutMqController {

    @Autowired
    private PublisherApi publisherApi;

    @RequestMapping("/fanout/{message}/{count}")
    public String fanout(@PathVariable("message") String message, @PathVariable("count") int count) {
        for (int i = 1; i <= count; i++) {
            publisherApi.publishMail(message);
        }
        System.out.println("########### publishMail 消息发送成功##################");
        return "消息发送成功";
    }
}
