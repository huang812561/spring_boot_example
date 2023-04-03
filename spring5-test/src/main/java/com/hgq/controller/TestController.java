package com.hgq.controller;

import com.hgq.bean.UserBean;
import com.hgq.bean.UserParam;
import com.hgq.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: hgq
 * @time: 2023/3/22 15:04
 */
@RestController
public class TestController {

    @Autowired
    private TestService testService;

    @PostMapping("query")
    public UserBean query(@RequestBody UserParam userParam) {
        return testService.query(userParam);
    }

}
