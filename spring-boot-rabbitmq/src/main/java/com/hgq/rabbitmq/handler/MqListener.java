package com.hgq.rabbitmq.handler;

import com.rabbitmq.client.Channel;

/**
 * MQ消息监听接口
 *
 * @Author hgq
 * @Date: 2022-08-30 16:47
 * @since 1.0
 **/
public interface MqListener<T> {
    /**
     * 业务通过实现这个接口处理消息
     *
     * @param map
     * @param channel
     * @throws Exception
     */
    default void handler(T map, Channel channel) throws Exception {

    }
}
