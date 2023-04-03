package com.hgq.service;

import com.hgq.bean.UserBean;
import com.hgq.bean.UserParam;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @description:
 * @author: hgq
 * @time: 2023/3/22 15:06
 */
@Service
public class TestService {

    /**
     * 查询用户信息
     *
     * @param userParam
     * @return
     */
    public UserBean query(UserParam userParam) {
        UserBean userBean = new UserBean()
                .setUserId(1)
                .setUserName(Objects.isNull(userParam.getUserName()) ? "张三" : userParam.getUserName())
                .setPhone(Objects.isNull(userParam.getPhone()) ? "111111" : userParam.getPhone());
        return userBean;
    }

}
