package com.hgq.entity;

import lombok.Data;

/**
 * todo
 *
 * @Author hgq
 * @Date: 2022-08-01 19:37
 * @since 1.0
 **/
@Data
public class Header {

    private String token;
    private String version = "1.0";
}
