package com.hgq.respository;


import com.hgq.entity.SystemLog;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 系统日志DAO
 */
public interface SystemLogResponsitory extends CrudRepository<SystemLog, Long> {
    @Transactional
    @Query(value = "select * from system_log" ,nativeQuery = true)
    List<SystemLog> queryAll();
}
