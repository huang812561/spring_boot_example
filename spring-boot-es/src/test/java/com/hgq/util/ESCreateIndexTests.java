package com.hgq.util;

import com.alibaba.fastjson.JSONObject;
import com.hgq.util.ElasticSearchUtil;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class ESCreateIndexTests {

    @Test
    void contextLoads() {
    }

    @Test
    public void testCreateIndex() {
        String indexName = "idx_test_01";
        if (ElasticSearchUtil.createIndex(indexName)) {
            Assert.isTrue(true, "创建索引成功");
        } else {
            Assert.isTrue(false, "创建索引失败");
        }
    }

    /**
     * 创建索引 with 分片和副本数
     * 1. 分片一旦建立，分片的数量则不能修改。
     * 2. 副本主要是主分片的复制，每个分片有多少个副本数，可动态调整，增加高可用性和提高性能
     */
    @Test
    public void testCreateIndexWithShardNumAndRepNum() {
        String indexName = "idx_test_02";
        if (ElasticSearchUtil.createIndex(indexName, 5, 3)) {
            Assert.isTrue(true, "创建索引成功");
        } else {
            Assert.isTrue(false, "创建索引失败");
        }
    }

    @Test
    public void testIndexExists() {
        String indexName = "idx_test_02";
        if (ElasticSearchUtil.indexExists(indexName)) {
            Assert.isTrue(true, "索引已经存在");
        } else {
            Assert.isTrue(false, "索引不存在");
        }
    }

    @Test
    public void testDel() {
        String indexName = "idx_test_02";
        if (ElasticSearchUtil.delIndex(indexName)) {
            Assert.isTrue(true, "删除成功");
        } else {
            Assert.isTrue(true, "删除失败");
        }
    }

    /**
     * ES创建文档的四种方式
     *     1.参数为Map<String,Object>
     *     2.参数为json字符串
     *     3.参数为XContentBuilder
     */

    /**
     * ES创建文档——1.参数为Map<String,Object>
     */
    @Test
    public void testCreateIndexMapping1() {
        String indexName = "idx_test_01";
        String mapStr = "{\n" +
                "    \"properties\": {\n" +
                "        \"birthday\": {\n" +
                "            \"type\": \"date\",\n" +
                "            \"store\": true\n" +
                "        },\n" +
                "        \"hobby\": {\n" +
                "            \"type\": \"text\",\n" +
                "            \"store\": true\n" +
                "        },\n" +
                "        \"id\": {\n" +
                "            \"type\": \"long\",\n" +
                "            \"store\": true\n" +
                "        },\n" +
                "        \"name\": {\n" +
                "            \"type\": \"text\",\n" +
                "            \"store\": true\n" +
                "        }\n" +
                "    }\n" +
                "}\n";
        Map<String, Object> mappings = new HashMap<>();
        mappings = JSONObject.parseObject(mapStr, Map.class);
        //使用XContentBuilder拼接查询条件时，注意有开始就要有结尾

        //设置索引的setting
        Settings setting = Settings.builder().put("index.number_of_shards", 5).put("index.number_of_replicas",
                2).build();

        if (ElasticSearchUtil.createIndexMapping(indexName, setting, mappings)) {
            Assert.isTrue(true, "创建索引成功");
        } else {
            Assert.isTrue(false, "创建索引失败");
        }
    }

    /**
     * ES创建文档——2.参数为json字符串
     */
    @Test
    public void testCreateIndexMapping2() {
        String indexName = "idx_test_02";
        String mappings = "{\n" +
                "    \"properties\": {\n" +
                "        \"birthday\": {\n" +
                "            \"type\": \"date\",\n" +
                "            \"store\": true\n" +
                "        },\n" +
                "        \"hobby\": {\n" +
                "            \"type\": \"text\",\n" +
                "            \"store\": true\n" +
                "        },\n" +
                "        \"id\": {\n" +
                "            \"type\": \"long\",\n" +
                "            \"store\": true\n" +
                "        },\n" +
                "        \"name\": {\n" +
                "            \"type\": \"text\",\n" +
                "            \"store\": true\n" +
                "        }\n" +
                "    }\n" +
                "}\n";
        //设置索引的setting
        Settings setting = Settings.builder().put("index.number_of_shards", 5).put("index.number_of_replicas",
                2).build();

        if (ElasticSearchUtil.createIndexMapping(indexName, setting.toString(), mappings)) {
            Assert.isTrue(true, "创建索引成功");
        } else {
            Assert.isTrue(false, "创建索引失败");
        }
    }

    /**
     * 3. 创建文档 —— 参数为XContentBuilder
     *
     * @throws IOException
     */
    @Test
    public void testCreateIndexMapping3() throws IOException {
        String indexName = "idx_test_03";
        XContentBuilder builder = XContentFactory.jsonBuilder();
        builder.startObject();
        {
            builder.startObject("properties");
            {
                builder.startObject("user_id");
                {
                    builder.field("type", "long");
                }
                builder.endObject();

                builder.startObject("name");
                {
                    builder.field("type", "text");
                }
                builder.endObject();
            }
            builder.endObject();
        }
        builder.endObject();

        if (ElasticSearchUtil.createIndexMapping(indexName, builder)) {
            Assert.isTrue(true, "创建索引成功");
        } else {
            Assert.isTrue(false, "创建索引失败");
        }
    }


    @Test
    public void testGetIndexSettings() {
        String indexName = "idx_test_02";
        String indexSettings = ElasticSearchUtil.getIndexSettings(indexName);
        System.out.println("索引名：" + indexName + ",分片和副本信息：" + indexSettings);
    }


    @Test
    public void testUpdateIndex1() {
        String indexName = "idx_test_02";
        Settings settings = Settings.builder()
                .put("index.max_result_window", 10000000)//修改索引最大返回结果，默认1W
                .put("index.number_of_replicas", 5)
//                .put("index.number_of_shards", 10)    //分片数量必须在创建索引时设置好，后期不允许修改的，这里设置修改会异常
                .build();
        if(ElasticSearchUtil.updateIndexSetting(indexName, settings)){
            Assert.isTrue(true,"更新索引成功");
        }else{
            Assert.isTrue(false,"更新索引失败");
        }
    }

    @Test
    public void testUpdateIndex2() {
        String indexName = "idx_test_02";
        Settings settings = Settings.builder()
                .put("index.max_result_window", 10000000)//修改索引最大返回结果，默认1W
                .put("index.number_of_replicas", 2)
//                .put("index.number_of_shards", 10)    //分片数量必须在创建索引时设置好，后期不允许修改的，这里设置修改会异常
                .build();
        if(ElasticSearchUtil.updateIndexSetting(indexName, settings.toString())){
            Assert.isTrue(true,"更新索引成功");
        }else{
            Assert.isTrue(false,"更新索引失败");
        }
    }

}
