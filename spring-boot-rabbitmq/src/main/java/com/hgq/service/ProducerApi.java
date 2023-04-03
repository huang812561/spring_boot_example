package com.hgq.service;

/**
 * todo
 *
 * @Author hgq
 * @Date: 2022-07-07 14:52
 * @since 1.0
 **/
public interface ProducerApi {
    void sendMessage(String queue,String message);
}
