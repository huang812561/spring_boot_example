package com.hgq.rabbitmq.queue;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 延时队列
 *
 * @Author hgq
 * @Date: 2022-08-30 16:54
 * @since 1.0
 **/
@Component
public class DelayQueue {

    // routingKey
    private static final String DELAYED_ROUTING_KEY = "delayed.routingkey";
    // 延迟队列交换机
    private static final String DELAYED_EXCHANGE = "delayed.exchange";

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Resource
    private RabbitAdmin rabbitAdmin;

    /**
     * 发送延迟队列
     *
     * @param queueName
     * @param params
     * @param expiration
     */
    public void sendDelayQueue(String queueName, Object params, Integer expiration) {
        // 先创建一个队列
        Queue queue = new Queue(queueName);
        rabbitAdmin.declareQueue(queue);
        // 创建延迟队列交换机
        CustomExchange customExchange = createCustomExchange();
        rabbitAdmin.declareExchange(customExchange);
        // 将队列和交换机绑定
        Binding binding = BindingBuilder.bind(queue).to(customExchange).with(DELAYED_ROUTING_KEY).noargs();
        rabbitAdmin.declareBinding(binding);
        // 发送延迟消息
        rabbitTemplate.convertAndSend(DELAYED_EXCHANGE, DELAYED_ROUTING_KEY, params, msg -> {
            // 发送消息的时候 延迟时长
            msg.getMessageProperties().setDelay(expiration);
            return msg;
        });
    }

    private CustomExchange createCustomExchange() {
        /**
         * 参数说明：
         * 1.交换机的名称
         * 2.交换机的类型
         * 3.是否需要持久化
         * 4.是否自动删除
         * 5.其它参数
         */
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-delayed-type", "direct");
        return new CustomExchange(DELAYED_EXCHANGE, "x-delayed-message", true, false, arguments);

    }


}
