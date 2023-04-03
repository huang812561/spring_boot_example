package com.hgq.service;

import com.hgq.entity.RetryInvorkParam;
import com.hgq.util.DateUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * todo
 *
 * @Author hgq
 * @Date: 2022-05-24 17:19
 * @since 1.0
 **/
@Component
public class RetryScheduledEngine extends TimerTask implements InitializingBean {
    private static final List<RetryInvorkParam> invorkParamList = new ArrayList<>();

    @Autowired
    private AsyncTaskExecutor asyncTaskExecutor;

    @Override
    public void run() {
        this.startEngine();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Timer timer = new Timer();
        timer.schedule(this, 5000, 1000);

    }

    /**
     * 开始任务
     */
    private synchronized void startEngine() {
        if (!invorkParamList.isEmpty()) {
            Iterator<RetryInvorkParam> iterator = invorkParamList.iterator();
            while (iterator.hasNext()) {
                RetryInvorkParam next = iterator.next();
                if (next.isEnd()) {
                    iterator.remove();
                    break;
                }

                Date now = new Date();
                Date nextInvokerTime = next.getNextInvorkTime();
                if (null != nextInvokerTime && nextInvokerTime.before(now)) {
                    long currentTimes = next.getCurrentTimes();
                    currentTimes++;
                    next.setCurrentTimes(currentTimes);
                    if (currentTimes >= next.getRetryTimes()) {
                        next.setEnd(true);
                    }

                    nextInvokerTime = DateUtil.addSeconds(nextInvokerTime, next.getDelayed());
                    next.setNextInvorkTime(nextInvokerTime);
                }
                asyncTaskExecutor.submit(new RetryRunning(next));
            }
        }
    }

    /**
     * 提交任务
     *
     * @param param
     */
    public void submit(RetryInvorkParam param) {
        // 计算执行时间
        Date now = new Date();
        if (param.getDelayed() > 0) {
            param.setNextInvorkTime(DateUtil.addSeconds(now, Long.valueOf(param.getDelayed()).intValue()));
        } else {
            param.setNextInvorkTime(now);
        }
        invorkParamList.add(param);
    }
}
