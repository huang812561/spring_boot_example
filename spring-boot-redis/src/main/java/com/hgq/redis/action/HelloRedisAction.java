package com.hgq.redis.action;

import com.alibaba.fastjson.JSON;
import com.hgq.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author huang_guoqiang
 * @desc
 * @date 2018/2/8 16:07
 */
@RestController
@Slf4j
public class HelloRedisAction {

    @Resource(name = "redisService")
    private RedisService redisService;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public static final String ORDER_REPEAT_BALANCE = "orderRepeatBalance";

    @RequestMapping(value = "testList")
    public String testList() throws Exception {
        try {
            List<Object> userIdList = getList("charge:noUnpaidCheckUserId");
            log.info("noUnpaidCheckUserId, list:{}", JSON.toJSONString(userIdList));
        }catch (Exception e){
            log.error("test redis list");
        }
        return "success";
    }

    public List<Object> getList(final String key) {
        List<Object> result = null;
        ListOperations<String, Object> operations = this.redisTemplate.opsForList();
        result = operations.range(key, 0L, -1L);
        return result;
    }



    @RequestMapping(value = "test")
    public String putString() throws Exception {
        //00000000339767534202204081527123
        //报错：com.fasterxml.jackson.core.JsonParseException: Invalid numeric value: Leading zeroes not allowed
        //MA002TMQX_8830492207221120219930
        //报错：com.fasterxml.jackson.core.JsonParseException: Unrecognized token 'MA002TMQX_8830492207221120219930': was expecting (JSON String, Number, Array, Object or token 'null', 'true' or 'false')
        String tradeSeq = "MA002TMQX_8830492207221120219930";
        String tradeSeq2 = "MA002TMQX_8830492207221120219930";
//        String tradeSeq2 = "" + "MA002TMQX_8830492207221120219930" + "";
        String rediskey = ORDER_REPEAT_BALANCE + tradeSeq;
//        redisService.set(rediskey,tradeSeq2);
        lock(rediskey,tradeSeq2,120);
        redisService.get(rediskey);
        return "success";
    }


    public boolean lock(@NonNull String key, String value, @NonNull long timeout) {
        return (Boolean) this.redisTemplate.execute(new RedisCallback<Boolean>() {
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.set(key.getBytes(), value.getBytes(), Expiration.seconds(timeout), RedisStringCommands.SetOption.SET_IF_ABSENT);
            }
        });
    }

    public void test() throws Exception {
        //=====================testString======================
        redisService.set("name", "王四");
        System.out.println("redis info [name]:" + redisService.get("name"));
        redisService.expire("name", 100);

        redisService.set("age", 24, 20);
        System.out.println("key [name] is exist:" + redisService.hasKey("name"));
        System.out.println("redis timeout [name ]: " + redisService.getExpire("name"));
        System.out.println("redis info [age ]: " + redisService.get("age") + ",redis timeout [age]:" + redisService.getExpire("age"));

        System.out.println(redisService.set("address", "河北邯郸", 50));
        System.out.println(redisService.get("address"));

        redisService.set("age", 1000);
        System.out.println("age modify after, redis info [age ]: " + redisService.get("age") + ",redis timeout [age]:" + redisService.getExpire("age"));


        redisService.del("address");
        redisService.batchDel(Arrays.asList("address", "home"));

        redisService.set("class", 100);
        long incr = redisService.incr("a", 1);
        System.out.println("incr:" + incr);
        long decr = redisService.decr("b", 2);
        System.out.println("decr:" + decr);


        Map<String, Object> map = new HashMap<>();
        map.put("name", "王赛超");
        map.put("age", 24);
        map.put("address", "河北邯郸666");
        redisService.hmset("15532002725", map, 1000);
        System.out.println(redisService.hget("15532002725", "name"));
        System.out.println(redisService.hget("15532002725", "age"));
        //redis 不同版本 会存在没有此命令的错误 ERR unknown command 'HINCRBYFLOAT'
        //redisService.hdecr("15532002725","age",-10);
        System.out.println("修改后的age:" + redisService.hget("15532002725", "age"));
        redisService.del("15532002725");
        redisService.hset("15532002725", "address", "河北邯郸", 1000);
        redisService.hdel("15532002725", "address");

        System.out.println(redisService.sSetAndTime("15532002727", 1000, "haha"));
        System.out.println(redisService.sGet("15532002727"));
        System.out.println(redisService.sHasKey("15532002727", "name"));
        List<String> strList = new ArrayList<String>();
        strList.add("1");
        strList.add("2");
        strList.add("3");
        strList.add("4");
        redisService.lSet("strList", strList);
        System.out.println(redisService.lGetIndex("strList", 0));
        System.out.println(redisService.lGetIndex("strList", 1));
        System.out.println(redisService.lUpdateIndex("strList", 0, "4"));
        System.out.println(redisService.lRemove("strList", 2, "4"));

       /* System.out.println(redisService.lGet("15532002728",0,-1));
        System.out.println(redisService.lGetListSize("15532002728"));
        System.out.println(redisService.lGetIndex("15532002728",1));
        System.out.println(redisService.lRemove("15532002728",1,2));
        System.out.println(redisService.getExpire("15532002725"));

        System.out.println(redisService.hget("15532002725","name"));
        System.out.println(redisService.hmget("15532002725"));*/
    }

}
