package com.hgq.bean;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @description:
 * @author: hgq
 * @time: 2023/3/22 15:53
 */
@Data
@Accessors(chain = true)
public class UserParam {
    private int userId;
    private String userName;
    private String phone;
    
}
