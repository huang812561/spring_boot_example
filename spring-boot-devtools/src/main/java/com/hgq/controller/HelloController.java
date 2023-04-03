package com.hgq.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * todo
 *
 * @Author hgq
 * @Date: 2022-03-29 17:58
 * @since 1.0
 **/
@RestController
public class HelloController {

    @GetMapping("/hello/{name}")
    public String hello(@PathVariable("name") String name) {
        System.out.println("测试热部署：" + name);

        return "dev tools test :" + name;
    }
}
