package com.hgq.controller;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * todo
 *
 * @Author hgq
 * @Date: 2022-03-14 15:21
 * @since 1.0
 **/
@RestController
@Slf4j
public class TestController {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 创建索引
     *
     * @param indexName 索引名称
     * @return
     */
    @GetMapping("/create/{indexName}")
    public String create(@PathVariable("indexName") String indexName){
        CreateIndexRequest request = new CreateIndexRequest(indexName);
        RequestOptions options = RequestOptions.DEFAULT;
        try {
            CreateIndexResponse response = restHighLevelClient.indices().create(request, options);
            if (response.isAcknowledged()){
                log.info("创建索引成功");
                return "创建索引成功";
            }else{
                log.info("创建索引失败");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "创建索引失败";
    }


}
