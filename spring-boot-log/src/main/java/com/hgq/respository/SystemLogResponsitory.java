package com.hgq.respository;


import com.hgq.entity.SystemLog;
import org.springframework.data.repository.CrudRepository;

/**
 * 系统日志DAO
 */
public interface SystemLogResponsitory extends CrudRepository<SystemLog, Long> {
}
