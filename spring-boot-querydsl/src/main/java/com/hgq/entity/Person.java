package com.hgq.entity;

import lombok.Data;

import java.util.Date;

/**
 * todo
 *
 * @Author hgq
 * @Date: 2022-07-29 17:51
 * @since 1.0
 **/
@Data
public class Person {

    private Integer id;
    private String name;
    private int age;
    private Date date;
}
