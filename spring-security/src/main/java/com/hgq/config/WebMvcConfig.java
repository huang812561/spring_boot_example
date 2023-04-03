package com.hgq.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ClassName com.hgq.config.WebMvcConfig
 * @Description: TODO
 * @Author: hgq
 * @Date: 2020-08-15 14:33
 * @Version: 1.0
 */
@Component
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("redirect:/login");
    }

}
