package com.hgq.controller;

import com.hgq.service.PublisherApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 3. 主题交换机 Topic Exchange
 *      这个交换机其实跟直连交换机流程差不多，但是它的特点就是在它的路由键和绑定键之间是有规则的。
 * 简单地介绍下规则：
 *      a. *  (星号) 用来表示一个单词 (必须出现的)
 *      b. #  (#号) 用来表示任意数量（零个或多个）单词
 *      通配的绑定键是跟队列进行绑定的，举个小例子
 *              队列Q1 绑定键为 *.TT.*          队列Q2绑定键为  TT.#
 *              如果一条消息携带的路由键为 A.TT.B，那么队列Q1将会收到；
 *              如果一条消息携带的路由键为TT.AA.BB，那么队列Q2将会收到；
 *
 * 主题交换机是非常强大的，为啥这么膨胀？
 * 当一个队列的绑定键为 "#"（井号） 的时候，这个队列将会无视消息的路由键，接收所有的消息。
 * 当 * (星号) 和 # (井号) 这两个特殊字符都未在绑定键中出现的时候，此时主题交换机就拥有的直连交换机的行为。
 * 所以主题交换机也就实现了扇形交换机的功能，和直连交换机的功能。
 *
 * 另外还有 【 Header Exchange 头交换机 、 Default Exchange 默认交换机 、Dead Letter Exchange 死信交换机 】
 *
 * @Author hgq
 * @Date: 2022-07-07 17:24
 * @since 1.0
 **/
@RestController
public class TopicMqController {

    @Autowired
    private PublisherApi publisherApi;


    @RequestMapping("/myTopic/{routingKey}/{message}/{count}")
    public String myTopic(@PathVariable("routingKey") String routingKey, @PathVariable("message") String message, @PathVariable("count") int count) {
        for (int i = 1; i <= count; i++) {
            publisherApi.sendTopicMail(routingKey, message);
        }
        System.out.println("########### sendtopicMail 消息发送成功##################");
        return "消息发送成功";
    }
}
