package com.hgq.config;

import com.hgq.rabbitmq.listener.custom.MyMessageListener;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 接收消息方式
 * 1. 自定义消息监听配置  MessageListenerConfig
 * 2. 使用注解@RabbitListener进行消息接收，RabbitMqConsumer
 *
 * @Author hgq
 * @Date: 2022-07-07 11:19
 * @since 1.0
 **/
@Configuration
public class MessageListenerConfig {

    @Autowired
    private CachingConnectionFactory cachingConnectionFactory;

    @Autowired
    private MyMessageListener myMessageListener;

    @Bean
    public Exchange orderExchange() {
        return ExchangeBuilder.directExchange("orderEchange").build();
    }

    @Bean
    public Queue orderQueue() {
        return new Queue("orderQueue");
    }

    /**
     * 配置listenercontainer,添加自定义listener
     *
     * @return
     */
    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(cachingConnectionFactory);
        container.setConcurrentConsumers(1);
        container.setMaxConcurrentConsumers(10);
//        RabbitMq默认是自动确认，这里改为手动确认
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
//      设置queue
        container.setQueues(orderQueue());
//        container.setQueueNames(orderQueue().getName());
        container.setMessageListener(myMessageListener);

        return container;
    }
}
