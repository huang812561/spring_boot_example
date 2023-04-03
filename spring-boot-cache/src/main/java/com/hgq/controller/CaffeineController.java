package com.hgq.controller;

import com.hgq.service.CaffeineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: hgq
 * @time: 2023/3/22 10:36
 */
@RestController
public class CaffeineController {

    @Autowired
    private CaffeineService caffeineService;

    @GetMapping("/put/{id}")
    public String put(@PathVariable("id") String id) {
        return caffeineService.put(id);
    }

    @GetMapping("/query/{id}")
    public String query(@PathVariable("id") String id) {
        return caffeineService.query(id);
    }

}
