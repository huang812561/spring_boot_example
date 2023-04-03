package com.hgq.util;

import com.hgq.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * todo
 *
 * @Author hgq
 * @Date: 2022-04-12 14:09
 * @since 1.0
 **/
@SpringBootTest
class ElasticSearchTest {

    @Test
    void addOrUpdateDoc() {
        User user = User.builder()
                .id(1)
                .name("张三")
                .hobby("南京市建邺区")
                .birthday(new Date())
                .build();
        String indexName = "idx_test_01";
        if (ElasticSearchUtil.addOrUpdateDoc(indexName, String.valueOf(user.getId()), user)) {
            Assert.isTrue(true, "新增文档成功");
        } else {
            Assert.isTrue(true, "新增文档失败");

        }
    }

    @Test
    void batchAddDocs() {

        String indexName = "idx_test_01";
        User user1 = User.builder()
                .id(1)
                .name("张三")
                .hobby("南京市建邺区")
                .birthday(new Date())
                .build();
        User user2 = User.builder()
                .id(2)
                .name("李四")
                .hobby("南京市雨花区")
                .birthday(new Date())
                .build();
        User user3 = User.builder()
                .id(3)
                .name("王五")
                .hobby("南京市秦淮区")
                .birthday(new Date())
                .build();
        List<User> list = new ArrayList<>();
        list.add(user1);
        list.add(user2);
        list.add(user3);
        Map<Long, Object> userMap = list.stream().collect(Collectors.toMap(User::getId, obj -> obj,
                (key1, key2) -> key1));
        if (ElasticSearchUtil.batchAddDocs(indexName, userMap)) {
            Assert.isTrue(true, "批量新增文档成功");
        } else {
            Assert.isTrue(false, "批量新增文档失败");
        }

    }

    @Test
    void batchUpdateDocs() {

    }

    @Test
    void delDoc() {

    }

    @Test
    void batchDelDocsByQuery() {
    }

    @Test
    void delDocsByQuery() {
    }

    @Test
    void searchOneDoc() {
    }

    @Test
    void searchDocsWithTermQuery() {
    }

    @Test
    void searchDocsWithMatchQuery() {
    }

    @Test
    void searchDocsWithPrefixQuery() {
    }

    @Test
    void searchDocsByQuery() {
    }

    @Test
    void testSearchDocsByQuery() {
    }
}