package com.hgq.read;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * todo
 *
 * @Author hgq
 * @Date: 2022-05-12 11:40
 * @since 1.0
 **/
@Slf4j
public class DemoDao {

    /**
     * 入库保存
     *
     * @param list
     */
    public void save(List<DemoData> list) {
        log.info("{}条数据，开始存储数据库！", list.size());
        log.info("存储数据库成功！");
    }
}
