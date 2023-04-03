package com.hgq.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.lang.NonNull;

/**
 * todo
 *
 * @Author hgq
 * @Date: 2022-04-11 17:48
 * @since 1.0
 **/
@Slf4j
@SpringBootTest
public class TestAction {


    @Autowired
    private RedisTemplate redisTemplate;


    @Test
    public void string3() {

        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);

        String tradeSeq = "10F2C004220411170844388061215402";
        String key = "order_balance";

//        redisTemplate.opsForValue().set("order_balance", tradeSeq);
        Object king = new Object();
//        king = jackson2JsonRedisSerializer.deserialize(tradeSeq.getBytes(StandardCharsets.UTF_8));
        if(lock("order_balance", tradeSeq,100)){
            king = redisTemplate.opsForValue().get("order_balance");
        }else {
            king = redisTemplate.opsForValue().get("order_balance");
        }
        System.out.println(king);

        redisTemplate.execute(new RedisCallback<Boolean>() {
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.set(key.getBytes(), tradeSeq.getBytes(), Expiration.seconds(1000),
                        RedisStringCommands.SetOption.SET_IF_ABSENT);
            }
        });

        king = redisTemplate.opsForValue().get(key);
        System.out.println(king);
    }

    public boolean lock(@NonNull String key, String value, @NonNull long timeout) {
        return (Boolean) this.redisTemplate.execute(new RedisCallback<Boolean>() {
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.set(key.getBytes(), value.getBytes(), Expiration.seconds(timeout), RedisStringCommands.SetOption.SET_IF_ABSENT);
            }
        });
    }
}
