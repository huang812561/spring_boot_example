package com.hgq.service;

import com.hgq.entity.RetryInvorkParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * todo
 *
 * @Author hgq
 * @Date: 2022-05-24 17:45
 * @since 1.0
 **/
public class RetryRunning implements Runnable {
    private final static Logger logger = LoggerFactory.getLogger(RetryRunning.class);
    /**
     * 执行的参数
     */
    private RetryInvorkParam invorkParam;


    public RetryRunning(RetryInvorkParam invorkParam) {
        this.invorkParam = invorkParam;
    }

    @Override
    public void run() {
        Method method = invorkParam.getInvorkMethod();
        Object[] args = invorkParam.getArgs();
        Object target = invorkParam.getTarget();
        Object result = null;
        try {
            result = method.invoke(target,args);
        } catch (Exception e) {
            /**
             * 此类异常直接抛出，不进行重试
             */
            for (Class exClass: invorkParam.getNeedThrowExceptions()) {
                if(exClass == e.getClass()){
                    logger.error("执行方法错误,不重试, {}", invorkParam, e);
                    invorkParam.setEnd(true);
                }
            }
        }

        if (result != null && result instanceof Boolean && (boolean) result == true) {
            invorkParam.setEnd(true);
        }
        if (invorkParam.isEnd()) {
            logger.info("@Retry method = {}, 重复执行了{}次，已经执行结束，param = {}",
                    target.getClass() + "." + method.getName(), invorkParam.getCurrentTimes(), invorkParam);
            return;
        }

        logger.info("@Retry 已经完成了{}方法，第{}次执行，总共需要执行{}次", target.getClass() + "." + method.getName(),
                invorkParam.getCurrentTimes(), invorkParam.getRetryTimes());

    }
}
