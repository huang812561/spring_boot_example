package com.hgq.redis.Util;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * todo
 *
 * @Author hgq
 * @Date: 2022-03-25 17:47
 * @since 1.0
 **/
public class RandomNameUtil {
    public static void main(String[] args) {
        /*Set<String> set = new HashSet<>();
        for (int i = 0; i < 1000000; i++) {
            set.add(getStringRandom(10));
        }
        System.out.println(set.size());*/

        System.out.println(getStringRandom(10));

        Set<String> set = new HashSet<>();
        for (int i = 0; i < 10000; i++) {
            set.add(getStringRandom(10));
        }
        System.out.println(set.size());

    }

    /**
     *
     * @param length
     * @return
     */
    public static String getStringRandom(int length) {
        return RandomStringUtils.random(length,"ABCDEFGHIJKMNOPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz23456789");
    }
}
