package com.hgq.service;

import com.hgq.entity.EmployeeEntity;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import static com.hgq.constants.CacheConstants.EMPLOYEE_CACHE_MANAGER;

/**
 * todo
 *
 * @Author hgq
 * @Date: 2022-03-28 15:21
 * @since 1.0
 **/
@Service
public class EmployeeService {

    /**
     * 获取员工数据
     *
     * @param empId
     * @return
     */

//    @Cacheable( value = "employee")
//    @Cacheable( value = "employee", key = "#root.args[0]")
//    @Cacheable(value = "employee", key = "#root.targetClass")
    @Cacheable(cacheManager = EMPLOYEE_CACHE_MANAGER, value = "employee", key = "#root.args[0]")
    public EmployeeEntity queryEmp(int empId) {
        System.out.println("根据不同的empId:" + empId + "获取不同员工数据");
        EmployeeEntity employee = new EmployeeEntity();
        employee.setEmpId(empId);
        employee.setEmpName("名称" + empId);
        return employee;
    }

    /**
     * 获取员工数据
     *
     * @param empId
     * @return
     */
    @Cacheable(cacheManager = EMPLOYEE_CACHE_MANAGER, value = "employee", key = "#root.args[0]")
    public EmployeeEntity queryEmp2(int empId) {
        System.out.println("根据不同的empId:" + empId + "获取不同员工数据");
        EmployeeEntity employee = new EmployeeEntity();
        employee.setEmpId(empId);
        employee.setEmpName("名称" + empId);
        return employee;
    }

    /**
     * 更新员工
     *
     * @param id
     * @param name
     * @return
     */
    @CachePut(value = "employee", key = "#root.args[0]")
    public EmployeeEntity updateEmp(int id, String name) {
        EmployeeEntity employee = new EmployeeEntity();
        employee.setEmpId(id);
        employee.setEmpName("名称" + name);
        return employee;
    }

    /**
     * 删除缓存
     *
     * @param id
     */
    @CacheEvict(value = "employee", key = "#root.args[0]")
    public void delEmp(int id) {

    }

    @Caching(cacheable = @Cacheable(value = "employee", key = "#root.args[0]"), put = @CachePut(value = "employee", key = "#root.args[0]"), evict = @CacheEvict(value = "employee", key = "#root.args[0]"))
    public void caching(int id) {

    }
}
