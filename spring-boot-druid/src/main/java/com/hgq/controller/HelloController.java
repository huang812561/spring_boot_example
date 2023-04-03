package com.hgq.controller;


import com.google.gson.Gson;
import com.hgq.component.annotation.SysLog;
import com.hgq.entity.SystemLog;
import com.hgq.service.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 测试
 * @Auther: hp
 * @Date: 2019/10/8 14:47
 */
@RestController
public class HelloController {

    @Autowired
    private SysLogService sysLogService;

    @RequestMapping("query")
    @SysLog(service = "log server", moudle = "日志模块", method = "query()", desc = "query log")
    public String query() {
        return new Gson().toJson(sysLogService.query());
    }


    @RequestMapping("/delete/{id}")
    @SysLog(service = "日志服务", moudle = "日志模块", method = "delete 方法", desc = "delete log")
    public String delete(@PathVariable("id") int id) {
        sysLogService.delete(id);
        return "success";
    }

}
