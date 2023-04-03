package com.hgq.redis.component.aop;

import com.google.common.collect.ImmutableList;
import com.hgq.redis.component.annotation.RedisCountLimit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.lang.reflect.Method;

/**
 * 计数器限流AOP
 *
 * @Author hgq
 * @Date: 2022-03-25 16:10
 * @since 1.0
 **/
//@Aspect
//@Component
@Slf4j
public class RedisLimitCountAspect {

    //待执行的lua脚本
    private final static String REDIS_SCRIPT = buildLuaScript();

    @Autowired
    private RedisTemplate redisTemplate;

    @Pointcut(value = "@annotation(com.hgq.redis.component.annotation.RedisCountLimit)")
    public void executeRedisLimit(){
    }


    @Before("executeRedisLimit()")
    public void executeBefore(JoinPoint joinPoint){

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        RedisCountLimit redisLimit = method.getAnnotation(RedisCountLimit.class);
        RedisCountLimit.LimitType limitType = redisLimit.limitType();
        int limitPeriod = redisLimit.period();
        int limitCount = redisLimit.count();
        String key ;
        switch (limitType){
            case CUSTOMER:
                key = redisLimit.key();
                break;
            case IP:
                key = redisLimit.key();
                break;
            default:
                key = "";
                break;
        }

        //执行lua脚本
        ImmutableList<String> keys = ImmutableList.of(StringUtils.join(redisLimit.prefix(), key));
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(REDIS_SCRIPT, Long.class);
        Long count = (Long) redisTemplate.execute(redisScript, keys, limitCount, String.valueOf(limitPeriod));
        log.info("当前并发数量为:[{}],限流数量为:[{}]", count,limitCount);
        if(count ==null || count.intValue()>limitCount){
            throw new RuntimeException("超过限流数量!");
        }

    }

    /**
     * 计数器/时间窗口法
     * 有个缺陷,假如在时间窗的前1%的时间内流量就达到顶峰了,那么在时间窗内还有99%的时间系统即使能够继续提供服务,
     * 还是会被限流算法的这种缺陷阻断在门外,这种缺陷也被称为"突刺效应"
     * 构建lua脚本
     * @return lua脚本
     */
    private static String buildLuaScript() {
        StringBuilder lua = new StringBuilder();
        lua.append("local c")
                .append("\nc = redis.call('get', KEYS[1])")
                // 调用超过最大值，则直接返回
                .append("\nif c and c > ARGV[1] then")
                .append("\nreturn c;")
                .append("\nend")
                // 执行计算器自加
                .append("\nc = redis.call('incr', KEYS[1])")
                .append("\nif tonumber(c) == 1 then")
                // 从第一次调用开始限流，设置对应键值的过期
                .append("\nredis.call('expire', KEYS[1], ARGV[2])")
                .append("\nend")
                .append("\nreturn c;");
        return lua.toString();
    }
}
