package com.hgq.util;

import lombok.SneakyThrows;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 重试1
 *
 * @Author hgq
 * @Date: 2022-05-24 13:36
 * @since 1.0
 **/
public class RetryTest1 {
    private static MyThreadLocal retryCount = new MyThreadLocal();

    public static void main(String[] args) {
        System.out.println("sum = " + sum(100));
    }

    /**
     * 求和
     *
     * @param n
     * @return
     */
    @SneakyThrows
    public static int sum(int n) {
        int sum = 0;
        try {
            for (int i = 1; i <= n; i++) {
                sum += i;
            }
            int num = ThreadLocalRandom.current().nextInt(100);
            if (num % 10 >= 0) {
                throw new RuntimeException("随机异常");
            }
        } catch (Exception e) {
            /**
             * 尝试3次
             */
            if (retryCount.get().incrementAndGet() <= 3) {
                Thread.sleep(3L);
                System.out.println("重试次数：" + retryCount.get().get());
                sum(n);
            }
            throw e;
        }
        return sum;
    }
}
