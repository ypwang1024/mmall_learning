package com.mmall.task;

import com.mmall.common.ConstValue;
import com.mmall.service.IOrderService;
import com.mmall.util.PropertiesUtil;
import com.mmall.util.RedisShardedPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @program: mmall
 * @description: 使用spring schedule 实现定时关单
 * @author: ypwang
 * @create: 2019-05-28 00:31
 **/
@Slf4j
@Component
public class CloseOrderTask {

    @Autowired
    private IOrderService iOrderService;

    // @Scheduled(cron = "0 */1 * * * ?") // 表示每一分钟
    public void closeOrderTaskV1() {
        log.info("关闭订单定时任务启动{}", new Date().toString());
        int hours = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour", "2"));
        iOrderService.closeOrder(hours);
        log.info("关闭订单定时任务完成{}", new Date().toString());
    }

    @Scheduled(cron = "0 */1 * * * ?") // 表示每一分钟
    public void closeOrderTaskV2() {
        log.info("关闭订单定时任务启动{}", new Date().toString());
        Long timeOut = Long.parseLong(PropertiesUtil.getProperty("lock.timeout", "5000"));
        Long setnxTimeOut = RedisShardedPoolUtil.setNx(ConstValue.RedisLock.CLOSE_ORDER_TASK_LOCK, String.valueOf(System.currentTimeMillis() + timeOut));
        if (setnxTimeOut != null && setnxTimeOut.intValue() == 1) {
            // 返回值是 1， 表示设置成功，获取锁
            closeOrder(ConstValue.RedisLock.CLOSE_ORDER_TASK_LOCK);
        } else {
            log.info("没有获取到分布式锁{}", ConstValue.RedisLock.CLOSE_ORDER_TASK_LOCK);
        }
        log.info("关闭订单定时任务完成{}", new Date().toString());
    }

    private void closeOrder(String lockName)
    {
        // 为了方便测试，锁的有效期设置为50S
        RedisShardedPoolUtil.expire(lockName, 50);
        log.info("获取{}， ThreadName：{}", lockName, Thread.currentThread().getName());
        int hours = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour", "2"));
        iOrderService.closeOrder(hours);
        RedisShardedPoolUtil.delete(lockName);
        log.info("释放{}， ThreadName：{}", lockName, Thread.currentThread().getName());
        log.info("=======================================================");
    }
}
