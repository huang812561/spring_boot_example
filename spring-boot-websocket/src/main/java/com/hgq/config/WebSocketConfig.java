package com.hgq.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * 开启WebSocket支持
 *
 * @Author hgq
 * @Date: 2022-08-24 18:54
 * @since 1.0
 **/
@Configuration
public class WebSocketConfig {


    /**
     * 扫描并注册带有@ServerEnpoint注解的所有服务端
     *
     * @return
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }
}
