package com.hgq.util;

import java.io.File;

/**
 * todo
 *
 * @Author hgq
 * @Date: 2022-05-12 09:40
 * @since 1.0
 **/
public class TestFileUtil {

    public static String getPath() {
        return TestFileUtil.class.getResource("/").getPath();
    }

    public static void main(String[] args) {
        System.out.println(getPath());
    }

    public static File createNewFile(String pathName) {
        File file = new File(getPath() + pathName);
        if (file.exists()) {
            file.delete();
        } else {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
        }
        return file;
    }

    /**
     *
     * @param pathName
     * @return
     */
    public static File readFile(String pathName) {
        return new File(getPath() + pathName);
    }
}
