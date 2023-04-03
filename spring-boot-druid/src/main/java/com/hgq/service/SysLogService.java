package com.hgq.service;


import com.hgq.entity.SystemLog;
import com.hgq.respository.SystemLogResponsitory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 日志Service
 */
@Service
@Slf4j
public class SysLogService {

    @Resource
    private SystemLogResponsitory systemLogResponsitory;

    /**
     * 保存系统日志
     * @param sysLogBO
     */
    public void saveLog(SystemLog sysLogBO){
        systemLogResponsitory.save(sysLogBO);
    }

    /**
     * 查询所有系统日志
     * @return
     */
    public List<SystemLog> query() {
        systemLogResponsitory.queryAll();
        return (List<SystemLog>) systemLogResponsitory.findAll();
    }

    /**
     * 更新日志
     * @param sysLogBO
     */
    public SystemLog update(SystemLog sysLogBO) {
        return systemLogResponsitory.save(sysLogBO);
    }

    public void delete(int id) {
        SystemLog log = new SystemLog();
        log.setId(id);
        systemLogResponsitory.delete(log);
    }
}
