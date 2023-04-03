package com.hgq.controller;


import com.google.gson.Gson;
import com.hgq.component.annotation.SysLog;
import com.hgq.entity.SystemLog;
import com.hgq.service.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
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

    /**
     * @return
     */
    @RequestMapping(value = "/")
    @SysLog(moudle = "首页模块", method = "index 方法", desc = "hi remark")
    public String hi() {
        return "hi";
    }

    /**
     * @return
     */
    @RequestMapping(value = "/hello")
    @SysLog(moudle = "欢迎模块", method = "hello 方法", desc = "hello remark")
    public String hello() {
        return "hello";
    }

    @RequestMapping("query")
    @SysLog(service = "log server", moudle = "日志模块", method = "query()", desc = "query log")
    public String query() {
        return new Gson().toJson(sysLogService.query());
    }


    @RequestMapping("update")
    @SysLog(service = "日志服务", moudle = "日志模块", method = "update 方法", desc = "update log")
    public String update(@RequestBody SystemLog systemLog) {
        return new Gson().toJson(sysLogService.update(systemLog));
    }
}
