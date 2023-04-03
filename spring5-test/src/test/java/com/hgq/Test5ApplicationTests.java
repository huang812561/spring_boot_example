package com.hgq;

import com.hgq.bean.UserParam;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Duration;

@Slf4j
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class Test5ApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    /**
     * 每个单元测试方法执行前执行一遍（仅执行一次）
     */
    @BeforeAll
    public static void beforeAll() {
        log.info("beforeAll");
    }

    /**
     * 每个单元测试方法执行前执行一次
     */
    @BeforeEach
    public void before() {
        log.info("before");
    }

    /**
     * 每个单元测试方法执行后执行一次
     */
    @AfterEach
    public void after() {
        log.info("after");
    }


    @Test
    @DisplayName("测试断言equals")
    public void testEquals() {
        /**
         * 预期值一致，则继续执行
         * 当预期值不一致时则抛出异常
         */
        Assertions.assertTrue(3 < 4, "assert true fail");
        Assertions.assertFalse(3 < 4, "assert false fail");
    }

    @Test
    @DisplayName("测试断言为null")
    public void testNotNull() {
        Assertions.assertNotNull(new Object());
        Assertions.assertNotNull(null);
    }

    @Test
    @DisplayName("测试断言抛出异常")
    public void testThrows() {
        ArithmeticException arithmeticException = Assertions.assertThrows(ArithmeticException.class, () -> {
            int m = 5 / 0;
        });
        Assertions.assertEquals("/ by zero", arithmeticException.getMessage());

    }

    @Test
    @DisplayName("测试断言超时")
    public void testTimeout() {
        //Duration.of(2, ChronoUnit.SECONDS)
        String result = Assertions.assertTimeout(Duration.ofSeconds(2), () -> {
            Thread.sleep(3000L);
            return "timeout";
        });
        System.out.println(result);
    }

    @Test
    @DisplayName("测试组合断言")
    public void testAll() {
        Assertions.assertAll("测试充电场景", () -> {
            Assertions.assertTrue(true, "充电成功");
        }, () -> {
            Assertions.assertTrue(true, "订单结算");
        }, () -> {
            Assertions.assertTimeout(Duration.ofSeconds(1), () -> {
                Assertions.assertTrue(false, "扣款失败，余额不足");
            });
        });
    }

    @RepeatedTest(3)
    @DisplayName("重复性测试")
    public void testRepeat() {
        System.out.println("执行");
    }


    @ParameterizedTest
    @ValueSource(ints = {1, 2, 5})
    @DisplayName("参数测试")
    public void testParam(int a) {
        Assertions.assertTrue(a > 0 && a < 4, "超出范围");
    }

    @Test
    @DisplayName("mock 测试")
    public void testQuery() throws Exception {
        UserParam userParam = new UserParam().setUserId(1).setUserName("mock").setPhone("1111111111");
        Object asyncResult = mockMvc.perform(MockMvcRequestBuilders.post("/query", userParam).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getAsyncResult(2000);
        Assertions.assertNotNull(asyncResult, "对象不能为空");
    }


}
