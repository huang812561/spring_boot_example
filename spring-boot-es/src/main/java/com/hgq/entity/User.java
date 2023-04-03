package com.hgq.entity;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * todo
 *
 * @Author hgq
 * @Date: 2022-04-12 10:35
 * @since 1.0
 **/
@Data
@Builder
public class User {

    /**
     * 用户ID （主键）
     */
    private long id;
    private String name;
    private String hobby;
    private Date birthday;
}
