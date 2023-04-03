package com.hgq.entity;

import lombok.Data;

/**
 * 员工类
 *
 * @Author hgq
 * @Date: 2022-03-28 15:18
 * @since 1.0
 **/
@Data
public class EmployeeEntity {
    /**
     * 员工id
     */
    private int empId;
    /**
     * 员工名称
     */
    private String empName;
}
