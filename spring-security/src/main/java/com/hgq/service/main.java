package com.hgq.service;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @ClassName com.hgq.service.main
 * @Description: TODO
 * @Author: hgq
 * @Date: 2020-08-28 13:40
 * @Version: 1.0
 */
public class main {
    public static void main(String[] args) {
        String a = "$2a$10$9YVpi9nUWGQsaveWCvLdLeKo2mrE1T0YbUzKxdVp4h/c/kiA/vuQW";
        String b = "$2a$10$G96t61tlW1ExYPfgWKmvMuKhCVGcDzyprDasE0StnOkSOHEm7P5eC";
        boolean flag = BCrypt.checkpw("123",a);
        System.out.println("密码比对的结果："+flag);
    }
}
