package com.hgq.util;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.settings.get.GetSettingsRequest;
import org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

/**
 * ES 工具类
 *
 * @Author hgq
 * @Date: 2022-03-14 19:23
 * @since 1.0
 **/
@Component
@Slf4j
public class ElasticSearchUtil {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    private static RestHighLevelClient client;

    private static final int SHARDS_NUM = 3;
    private static final int REPLICAS_NUM = 2;

    @PostConstruct
    public void init() {
        client = this.restHighLevelClient;
    }

    /**
     * 创建索引成功
     *
     * @param indexName 索引名称
     * @return
     */
    public static boolean createIndex(String indexName) {
        CreateIndexRequest request = new CreateIndexRequest(indexName);
        try {
            CreateIndexResponse response = client.indices().create(request, RequestOptions.DEFAULT);
            request.settings(Settings.builder().put("index.number_of_shards", SHARDS_NUM).put("index.number_of_replicas", REPLICAS_NUM).build());
            if (response.isAcknowledged()) {
                log.info("创建索引成功，name={}", indexName);
                return true;
            } else {
                log.info("创建索引失败，name={}", indexName);
            }
        } catch (IOException e) {
            log.error("创建索引异常", e);
        }
        return false;
    }

    /**
     * 创建索引
     *
     * @param indexName
     * @param shardNum
     * @param replicasNum
     * @return
     */
    public static boolean createIndex(String indexName, int shardNum, int replicasNum) {
        CreateIndexResponse response = null;
        try {
            CreateIndexRequest request = new CreateIndexRequest(indexName);
            //设置分片数量
            request.settings(Settings.builder().put("index.number_of_shards", shardNum > 0 ? shardNum : SHARDS_NUM).put("index.number_of_replicas", replicasNum > 0 ? replicasNum : REPLICAS_NUM).build());
            response = client.indices().create(request, RequestOptions.DEFAULT);
            return response.isAcknowledged();
        } catch (IOException e) {
            log.error("ESUtil createIndex error", e);
        }
        return false;
    }

    /**
     * 创建索引及其映射
     *
     * @param indexName
     * @param setting
     * @param mapping
     * @return
     */
    public static boolean createIndexMapping(String indexName, String setting, String mapping) {
        CreateIndexResponse response = null;
        try {
            CreateIndexRequest request = new CreateIndexRequest(indexName);
            //设置分片数量
            if (StringUtils.hasText(setting)) {
                request.settings(setting, XContentType.JSON);
            } else {
                request.settings(Settings.builder().put("index.number_of_shards", SHARDS_NUM).put("index.number_of_replicas", REPLICAS_NUM).build());
            }
            request.mapping(mapping, XContentType.JSON);
            response = client.indices().create(request, RequestOptions.DEFAULT);
            if (response.isAcknowledged()) {
                return true;
            }
        } catch (IOException e) {
            log.error("ESUtil createIndex error", e);
        }
        return false;
    }

    /**
     * 创建索引及其映射
     *
     * @param indexName
     * @param setting
     * @param mapping
     * @return
     */
    public static boolean createIndexMapping(String indexName, Settings setting, Map<String, Object> mapping) {
        CreateIndexResponse response = null;
        try {
            CreateIndexRequest request = new CreateIndexRequest(indexName);
            //设置分片数量
            if (null != setting) {
                request.settings(setting);
                //request.settings(setting, XContentType.JSON);
            } else {
                request.settings(Settings.builder().put("index.number_of_shards", SHARDS_NUM).put("index.number_of_replicas", REPLICAS_NUM).build());
            }
            request.mapping(mapping);
            response = client.indices().create(request, RequestOptions.DEFAULT);
            return response.isAcknowledged();
        } catch (IOException e) {
            log.error("ESUtil createIndexMapping error", e);
        }
        return false;
    }

    /**
     * 创建映射
     *
     * @param indexName
     * @param mapping
     * @return
     */
    public static boolean createIndexMapping(String indexName, XContentBuilder mapping) {
        try {
            CreateIndexRequest request = new CreateIndexRequest(indexName);
            request.mapping(mapping);
            AcknowledgedResponse response = client.indices().create(request, RequestOptions.DEFAULT);
            return response.isAcknowledged();
        } catch (IOException e) {
            log.error("ESUtil createIndex error", e);
        }
        return false;
    }


    /**
     * 删除索引
     *
     * @param indexName
     * @return
     */
    public static boolean delIndex(String indexName) {
        try {
            DeleteIndexRequest delRequest = new DeleteIndexRequest(indexName);
            AcknowledgedResponse response = client.indices().delete(delRequest, RequestOptions.DEFAULT);
            return response.isAcknowledged();
        } catch (Exception e) {
            log.error("ESUtil delIndex error", e);
        }
        return false;
    }

    /**
     * 索引是否存在
     *
     * @param indexName
     * @return true 存在  false 不存在
     */
    public static boolean indexExists(String indexName) {
        try {
            GetIndexRequest request = new GetIndexRequest(indexName);
            return client.indices().exists(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("ESUtil indexExists error", e);
        }
        return false;
    }

    /**
     * 获取索引的setting信息 分片和副本
     *
     * @param indexName
     * @return
     */
    public static String getIndexSettings(String indexName) {
        try {
            GetSettingsRequest request = new GetSettingsRequest().indices(indexName);
            Settings settings = client.indices().getSettings(request, RequestOptions.DEFAULT).getIndexToSettings().get(indexName);
            return settings.toString();
        } catch (IOException e) {
            log.error("ESUtil getIndexSettings error", e);
        }
        return "";
    }

    /**
     * 更新索引的setting信息
     *
     * @param indexName
     * @param settings  JSON 字符串
     * @return
     */
    public static boolean updateIndexSetting(String indexName, String settings) {
        try {
            UpdateSettingsRequest request = new UpdateSettingsRequest(indexName);
            request.settings(settings, XContentType.JSON);
            AcknowledgedResponse response = client.indices().putSettings(request, RequestOptions.DEFAULT);
            return response.isAcknowledged();
        } catch (IOException e) {
            log.error("ESUtil updateIndexSetting error", e);
        }
        return false;
    }

    /**
     * 更新索引的setting信息
     *
     * @param indexName
     * @param settings
     * @return
     */
    public static boolean updateIndexSetting(String indexName, Settings settings) {
        try {
            UpdateSettingsRequest request = new UpdateSettingsRequest(indexName);
            request.settings(settings);
            AcknowledgedResponse response = client.indices().putSettings(request, RequestOptions.DEFAULT);
            return response.isAcknowledged();
        } catch (IOException e) {
            log.error("ESUtil updateIndexSetting error", e);
        }
        return false;
    }

    /**
     * 新增或更新文档
     *
     * @param indexName
     * @param id
     * @param data
     * @return
     */
    public static boolean addOrUpdateDoc(String indexName, String id, Object data) {
        if (null != data) {
            try {
                boolean exists = false;
                if (StringUtils.hasText(id)) {
                    GetRequest getRequest = new GetRequest(indexName, id);
                    exists = client.exists(getRequest, RequestOptions.DEFAULT);
                }

                if (exists) {
                    UpdateRequest updateRequest = new UpdateRequest(indexName, id).doc(JSON.toJSONString(data), XContentType.JSON);
                    UpdateResponse update = client.update(updateRequest, RequestOptions.DEFAULT);
                    log.info("ESUtil updateDocs indexName:{},response:{}", indexName, update);
                } else {
                    //id 推荐自定义
                    IndexRequest addRequest = new IndexRequest(indexName).id(id).source(JSON.toJSONString(data), XContentType.JSON);
                    //id 不设置，默认自动生成
                    //IndexRequest addRequest = new IndexRequest(indexName).source(JSON.toJSONString(data),XContentType.JSON);

                    IndexResponse index = client.index(addRequest, RequestOptions.DEFAULT);
                    log.info("ESUtil updateDocs indexName:{},response:{}", indexName, index);
                }
            } catch (IOException e) {
                log.error("ESUtil addOrUpdateDoc error", e);
            }
        }
        return false;
    }

    /**
     * 批量插入文档
     *
     * @param list      数据List
     * @param indexName 文档名称
     * @param dataMap
     * @return
     */
    public static boolean batchAddDocs(String indexName, Map<Long, Object> dataMap) {
        try {
            BulkRequest bulkRequest = new BulkRequest();
            for (Map.Entry<Long,Object> entry: dataMap.entrySet()){
                IndexRequest indexRequest = new IndexRequest();
                indexRequest.index(indexName).id(String.valueOf(entry.getKey())).source(JSON.toJSONString(entry.getValue()),
                        XContentType.JSON);
                bulkRequest.add(indexRequest);
            }
            BulkResponse bulk = client.bulk(bulkRequest, RequestOptions.DEFAULT);
            log.info("ESUtil batchAddDoc indexName:{},response:{}", indexName, bulk);
            return !bulk.hasFailures();
        } catch (IOException e) {
            log.error("ESUtil batchAddDoc error", e);
        }
        return false;
    }

    /**
     * 批量更新文档
     *
     * @param indexName
     * @param dataDoc
     * @return
     */
    public static boolean batchUpdateDocs(String indexName, Map<String, Object> dataDoc) {
        BulkRequest bulkRequest = new BulkRequest();
        try {
            dataDoc.forEach((id, obj) -> {
                IndexRequest request = new IndexRequest(indexName);
                request.source(JSON.toJSONString(obj), XContentType.JSON).id(id);
                bulkRequest.add(request);
            });
            BulkResponse bulk = client.bulk(bulkRequest, RequestOptions.DEFAULT);
            log.info("ESUtil batchUpdateDoc indexName:{},response:{}", indexName, bulk);
            return !bulk.hasFailures();
        } catch (IOException e) {
            log.error("ESUtil batchUpdateDoc error", e);
        }
        return false;
    }

    /**
     * 删除单个文档
     *
     * @param indexName
     * @param id
     * @return
     */
    public static boolean delDoc(String indexName, String id) {
        try {
            DeleteRequest deleteRequest = new DeleteRequest(indexName);
            deleteRequest.id(id);
            DeleteResponse delete = client.delete(deleteRequest, RequestOptions.DEFAULT);
            log.info("ESUtil delDoc indexName:{},response:{}", indexName, delete);
            return true;
        } catch (IOException e) {
            log.error("ESUtil delDoc error", e);
        }
        return false;
    }

    /**
     * 批量删除IDS条件查出来的数据
     *
     * @param indexName
     * @param ids
     * @return
     */
    public static boolean batchDelDocsByQuery(String indexName, String[] ids) {
        try {
            DeleteByQueryRequest request = new DeleteByQueryRequest(indexName);
            BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
            boolQueryBuilder.must(QueryBuilders.idsQuery().addIds(ids));
            BulkByScrollResponse response = client.deleteByQuery(request, RequestOptions.DEFAULT);
            log.info("ESUtil batchDelDocByQuery indexName:{},response:{}", indexName, response);
            return true;
        } catch (Exception e) {
            log.error("ESUtil batchDelDocByQuery，indexName={}", indexName, e);
        }
        return false;
    }

    /**
     * 批量删除条件查出来的数据
     *
     * @param indexName
     * @param name
     * @param text
     * @return
     */
    public static boolean delDocsByQuery(String indexName, String name, String text) {
        try {
            DeleteByQueryRequest request = new DeleteByQueryRequest(indexName);
            BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
            boolQueryBuilder.must(QueryBuilders.matchQuery(name, text));
            BulkByScrollResponse response = client.deleteByQuery(request, RequestOptions.DEFAULT);
            log.info("ESUtil delDocByQuery indexName:{},response:{}", indexName, response);
            return true;
        } catch (Exception e) {
            log.error("ESUtil delDocByQuery，indexName={}", indexName, e);
        }
        return false;
    }


    /**
     * 查询单条by id
     *
     * @param indexName
     * @param indexId
     * @return
     */
    public static Map<String, Object> searchOneDoc(String indexName, String indexId) {
        try {
            GetRequest getRequest = new GetRequest(indexName, indexId);
            GetResponse response = client.get(getRequest, RequestOptions.DEFAULT);
            return response.getSource();
        } catch (IOException e) {
            log.error("ESUtil searchOneDoc error", e);
        }
        return null;
    }

    /**
     * 根据条件查询数据(模糊匹配——关键字不分词)
     *
     * @param indexName
     * @param name
     * @param text
     * @return
     */
    public static Object[] searchDocsWithTermQuery(String indexName, String name, String text) {
        try {
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.termQuery(name, text));
            SearchRequest request = new SearchRequest(indexName);
            request.source(searchSourceBuilder);
            SearchResponse search = client.search(request, RequestOptions.DEFAULT);
            return search.getHits().getCollapseValues();
        } catch (IOException e) {
            log.error("ESUtil searchDocsByQuery error", e);
        }
        return null;
    }

    /**
     * 根据条件查询数据(模糊匹配——关键字分词)
     *
     * @param indexName
     * @param name
     * @param text
     * @return
     */
    public static Object[] searchDocsWithMatchQuery(String indexName, String name, String text) {
        try {
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.matchQuery(name, text));
            SearchRequest request = new SearchRequest(indexName);
            request.source(searchSourceBuilder);
            SearchResponse search = client.search(request, RequestOptions.DEFAULT);
            return search.getHits().getCollapseValues();
        } catch (IOException e) {
            log.error("ESUtil searchDocsByQuery error", e);
        }
        return null;
    }

    /**
     * 根据条件查询数据(前缀匹配规则)
     *
     * @param indexName
     * @param name
     * @param text
     * @return
     */
    public static Object[] searchDocsWithPrefixQuery(String indexName, String name, String text) {
        try {
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.prefixQuery(name, text));
            SearchRequest request = new SearchRequest(indexName);
            request.source(searchSourceBuilder);
            SearchResponse search = client.search(request, RequestOptions.DEFAULT);
            return search.getHits().getCollapseValues();
        } catch (IOException e) {
            log.error("ESUtil searchDocsByQuery error", e);
        }
        return null;
    }

    /**
     * 查询索引文档
     *
     * @param indexName
     * @param searchSourceBuilder
     * @return
     */
    public static List<String> searchDocsByQuery(String indexName, SearchSourceBuilder searchSourceBuilder) {
        List<String> resultList = new ArrayList<>();
        try {
            SearchRequest request = new SearchRequest();
            request.indices(indexName);
            request.source(searchSourceBuilder);
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            if (RestStatus.OK.equals(response.status()) && response.getHits().getTotalHits().value > 0) {
                SearchHits hits = response.getHits();
                hits.forEach(hit -> {
                    resultList.add(hit.getSourceAsString());
                });
            }
        } catch (IOException e) {
            log.error("ESUtil searchDocsByQuery error", e);
        }
        return resultList;
    }

    /**
     * 查询索引文档数据
     *
     * @param indexName
     * @param termQueryBuilder
     * @return
     */
    public static List<String> searchDocsByQuery(String indexName, TermQueryBuilder termQueryBuilder) {
        List<String> resultList = new ArrayList<>();
        try {
            SearchRequest request = new SearchRequest();
            request.indices(indexName);
            request.source(new SearchSourceBuilder().query(termQueryBuilder));
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            if (RestStatus.OK.equals(response.status()) && response.getHits().getTotalHits().value > 0) {
                SearchHits hits = response.getHits();
                hits.forEach(hit -> {
                    resultList.add(hit.getSourceAsString());
                });
            }
        } catch (IOException e) {
            log.error("ESUtil searchDocsByQuery error", e);
        }
        return resultList;

    }

}
