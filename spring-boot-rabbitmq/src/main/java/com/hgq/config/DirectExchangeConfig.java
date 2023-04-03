package com.hgq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * todo
 *
 * @Author hgq
 * @Date: 2022-07-07 14:18
 * @since 1.0
 **/
@Configuration
public class DirectExchangeConfig {

    @Autowired
    private RabbitMqProperties properties;

    /**
     * 设置交换机
     *
     * @return
     */
    @Bean
    public Exchange exchangeBean() {
        return new DirectExchange(properties.getExchangeName());
    }

    @Bean
    public Queue directQueue1() {
        Queue queue = new Queue(properties.getQueueOne());
        return queue;
    }

    @Bean
    public Queue directQueue2() {
        Queue queue = new Queue(properties.getQueueTwo());
        return queue;
    }

    @Bean
    public Queue directQueue3() {
        Queue queue = new Queue(properties.getQueueThree());
        return queue;
    }

    @Bean
    public Binding bindingOrder() {
        BindingBuilder.GenericArgumentsConfigurer order = BindingBuilder.bind(directQueue1()).to(exchangeBean()).with("order");
        return order.noargs();
    }

    @Bean
    public Binding bindingBase() {
        BindingBuilder.GenericArgumentsConfigurer order = BindingBuilder.bind(directQueue2()).to(exchangeBean()).with("base");
        return order.noargs();
    }

    @Bean
    public Binding bindingCharge() {
        BindingBuilder.GenericArgumentsConfigurer order = BindingBuilder.bind(directQueue3()).to(exchangeBean()).with("charge");
        return order.noargs();
    }

}
