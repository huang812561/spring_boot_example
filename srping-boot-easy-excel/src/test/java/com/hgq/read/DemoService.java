package com.hgq.read;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * todo
 *
 * @Author hgq
 * @Date: 2022-05-12 11:39
 * @since 1.0
 **/
public class DemoService {

    private DemoDao demoDao;

    public DemoService() {
        demoDao = new DemoDao();
    }

    public void doSomething(List<DemoData> list){
        //1. 业务处理
        doBesiness(list);
        //2. 保存入库
        demoDao.save(list);
    }

    private void doBesiness(List<DemoData> list) {

    }
}
