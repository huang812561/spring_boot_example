package com.hgq.redis.component.aop;

import com.hgq.redis.component.annotation.RedisTokenBucketLimit;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

/**
 * 令牌桶-限流AOP
 *
 * @Author hgq
 * @Date: 2022-03-25 16:10
 * @since 1.0
 **/
@Aspect
@Component
@Slf4j
public class RedisLimitTokenBucketAspect {

    //获取IP有可能获取到的常量
    private static final String UNKNOWN = "unknown";

    //待执行的lua脚本
    private static String REDIS_SCRIPT = buildLuaScript();
//    private static String REDIS_SCRIPT;

    static {
        try {
            InputStream resourceAsStream = RedisTokenBucketLimit.class.getResourceAsStream("/script/tokenBucket.lua");
            byte[] bytes = new byte[resourceAsStream.available()];
            resourceAsStream.read(bytes);
            System.out.println(new String(bytes));
            //REDIS_SCRIPT = new String(bytes);
        } catch (IOException e) {
            log.error("read tokenBucket.lua error {}", e);
        }
    }

    @Autowired
    private RedisTemplate redisTemplate;

    @Pointcut(value = "@annotation(com.hgq.redis.component.annotation.RedisTokenBucketLimit)")
    public void executeRedisLimit() {
    }


    @Before("executeRedisLimit()")
    public void executeBefore(JoinPoint joinPoint) {

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        RedisTokenBucketLimit tokenBucketLimit = method.getAnnotation(RedisTokenBucketLimit.class);

        DefaultRedisScript<List> redisScript = new DefaultRedisScript<>(REDIS_SCRIPT, List.class);
        List<String> keys = Arrays.asList(tokenBucketLimit.tokensKey(), tokenBucketLimit.timestampKey());
        Object[] args = new Object[]{
                tokenBucketLimit.rate(),
                tokenBucketLimit.capacity(),
                Instant.now().getEpochSecond(), //时区是UTC,不是UTC+8,此处不影响
                tokenBucketLimit.requested()
        };
        List<Long> resultList = (List<Long>) redisTemplate.execute(redisScript, keys, args);
        if (resultList.get(0) == 0) {
            throw new RuntimeException("超过限流数量");
        } else {
            log.info("令牌桶中剩余数量为：【{}】", resultList.get(1));
        }
    }

    /**
     * 基于令牌桶
     * local tokens_key = KEYS[1] 当前限流的标识,可以是ip,或者在spring cloud系统中,可以是一个服务的serviceID
     * local timestamp_key = KEYS[2] 令牌桶刷新的时间戳,后面会被用来计算当前产生的令牌数
     * <p>
     * local rate = tonumber(ARGV[1]) 令牌生产的速率,如每秒产生50个令牌
     * local capacity = tonumber(ARGV[2]) 令牌桶的容积大小,比如最大100个,那么系统最大可承载100个并发请求
     * local now = tonumber(ARGV[3]) 当前时间戳
     * local requested = tonumber(ARGV[4]) 当前请求的令牌数量,Spring Cloud Gateway中默认是1,也就是当前请求
     * <p>
     * -- 计算填满桶需要多长时间
     * local fill_time = capacity/rate
     * -- 得到填满桶的2倍时间作为redis中key时效的时间,避免冗余太多无用的key
     * local ttl = math.floor(fill_time*2) 令牌的有效期
     * <p>
     * <p>
     * -- 获取桶中剩余的令牌，如果桶是空的，就将他填满
     * local last_tokens = tonumber(redis.call("get", tokens_key))
     * if last_tokens == nil then
     * last_tokens = capacity
     * end
     * <p>
     * -- 获取当前令牌桶最后的刷新时间,如果为空,则设置为0
     * local last_refreshed = tonumber(redis.call("get", timestamp_key))
     * if last_refreshed == nil then
     * last_refreshed = 0
     * end
     * <p>
     * -- 计算最后一次刷新令牌到当前时间的时间差
     * local delta = math.max(0, now-last_refreshed)
     * <p>
     * -- 计算当前令牌数量，这个地方是最关键的地方，通过剩余令牌数 + 时间差内产生的令牌得到当前总令牌数量
     * local filled_tokens = math.min(capacity, last_tokens+(delta*rate))
     * <p>
     * --设置标识allowad接收当前令牌桶中的令牌数是否大于请求的令牌结果
     * local allowed = filled_tokens >= requested
     * <p>
     * --设置当前令牌数量
     * local new_tokens = filled_tokens
     * <p>
     * --如果allowed为true,则将当前令牌数量重置为桶中的令牌数-请求的令牌数,并且设置allowed_num标识为1
     * local allowed_num = 0
     * if allowed then
     * new_tokens = filled_tokens - requested
     * allowed_num = 1
     * end
     * <p>
     * --将当前令牌数量写回到redis中，并重置令牌桶的最后刷新时间
     * redis.call("setex", tokens_key, ttl, new_tokens)
     * redis.call("setex", timestamp_key, ttl, now)
     * <p>
     * --返回当前是否申请到了令牌，以及当前桶中剩余多少令牌
     * return { allowed_num, new_tokens }
     * <p>
     * https://www.cnblogs.com/myseries/p/12634560.html
     * Spring网关中是基于令牌桶+redis实现的网关分布式限流，具体的实现见下面两个代码：
     * lua脚本地址：resources/META-INF/scripts/request_rate_limiter.lua
     * RedisRateLimiter：gateway/filter/ratelimit/RedisRateLimiter.java
     * <p>
     * 通过返回值【0】是否等于1来判断本次流量是否通过，返回值【1】为令牌桶中剩余的令牌数。就上面这段代码没有看到任何令牌桶算法的
     * 影子对吧，所有的精华实现都在request_rate_limiter.lua脚本里面
     * https://gist.github.com/ptarjan/e38f45f2dfe601419ca3af937fff574d
     */
    private static String buildLuaScript(){
        StringBuilder lua = new StringBuilder();
        lua.append("\nlocal tokens_key = KEYS[1]")
                .append("\nlocal timestamp_key = KEYS[2]")
                .append("\nlocal rate = ARGV[1]")
                .append("\nlocal capacity = ARGV[2]")
                .append("\nlocal now = ARGV[3]")
                .append("\nlocal requested = ARGV[4]")
                .append("\nlocal fill_time = 4")
                .append("\nlocal ttl = math.floor(fill_time*2)")
                .append("\nlocal last_tokens = redis.call(\"get\", tokens_key)")
                .append("\nif last_tokens == nil then")
                .append("\nlast_tokens = capacity")
                .append("\nend")
                .append("\nlocal last_refreshed = redis.call(\"get\", timestamp_key)")
                .append("\nif last_refreshed == nil then")
                .append("\nlast_refreshed = 0")
                .append("\nend")
                .append("\nlocal delta = math.max(0, now-last_refreshed)")
                .append("\nlocal filled_tokens = math.min(capacity, last_tokens+(delta*rate))")
                .append("\nlocal allowed = filled_tokens >= requested")
                .append("\nlocal new_tokens = filled_tokens")
                .append("\nlocal allowed_num = 0")
                .append("\nif allowed then")
                .append("\nnew_tokens = filled_tokens - requested")
                .append("\nallowed_num = 1")
                .append("\nend")
                .append("\nredis.call(\"setex\", tokens_key, ttl, new_tokens)")
                .append("\nredis.call(\"setex\", timestamp_key, ttl, now)")
                .append("\nreturn { allowed_num, new_tokens }");
        return lua.toString();
    }
}
