package com.hgq.entity;

import lombok.Data;

/**
 * todo
 *
 * @Author hgq
 * @Date: 2022-08-24 19:22
 * @since 1.0
 **/
@Data
public class MessageVO {

    private String sid;
    private String toUserId;
    private String message;
}
