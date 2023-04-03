package com.hgq.entity;

import lombok.Data;

/**
 * todo
 *
 * @Author hgq
 * @Date: 2022-08-01 19:36
 * @since 1.0
 **/
@Data
public class RequestData<T> {

    private Header header;
    private T body;
}
