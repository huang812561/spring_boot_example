package com.hgq.controller;

import com.hgq.service.CaffeineService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@Slf4j
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringBootCacheApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CaffeineService caffeineService;

    @Test
    public void putActionTest() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.request(HttpMethod.GET, "/put/1");

        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        System.out.println("返回状态：" + mvcResult.getResponse().getStatus());
        System.out.println("返回内容：" + mvcResult.getResponse().getContentAsString());
    }


    @Test
    public void putTest() {
        String result = caffeineService.put("1");
        Assertions.assertNotNull(result, result);

    }

    @Test
    public void queryActionTest() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.request(HttpMethod.GET, "/query/1");

        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        System.out.println("返回状态：" + mvcResult.getResponse().getStatus());
        System.out.println("返回内容：" + mvcResult.getResponse().getContentAsString());

    }

    @Test
    public void queryTest() {
        String result = caffeineService.query("1");
        Assertions.assertNotNull(result, result);
    }


}
