package com.hgq.controller;

import com.hgq.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * todo
 *
 * @Author hgq
 * @Date: 2022-03-28 14:19
 * @since 1.0
 **/
@RestController
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/emp/{id}")
    public String getEmp(@PathVariable("id") int id) {
        return employeeService.queryEmp(id).getEmpName();
    }

    @GetMapping("/emp2/{id}")
    public String getEmp2(@PathVariable("id") int id) {
        return employeeService.queryEmp2(id).getEmpName();
    }

    @GetMapping("/update/{id}/{name}")
    public String updateEmp(@PathVariable("id")int id,@PathVariable("name")String name){
        return employeeService.updateEmp(id,name).getEmpName();
    }

    @GetMapping("/del/{id}")
    public int delEmp(@PathVariable int id){
        employeeService.delEmp(id);
        return id;
    }

    @GetMapping("/caching/{id}")
    public int caching(@PathVariable int id){
        employeeService.caching(id);
        return id;
    }


}
