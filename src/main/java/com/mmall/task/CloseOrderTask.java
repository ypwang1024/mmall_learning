package com.mmall.task;

import com.mmall.common.ConstValue;
import com.mmall.service.IOrderService;
import com.mmall.util.PropertiesUtil;
import com.mmall.util.RedisShardedPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
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

    @PreDestroy
    public void delLock()
    {
        // 第一重 防止死锁，设置key的有效期,可能由于各种客观存在的原因，该代码没有执行，如tomcat突然关闭，造成锁永久激活
        // 防止死锁 PreDestroy 注解在tomcat关闭前执行，删除redis分布式锁，但是如果锁非常多，会造成shutdown时间比较长
        RedisShardedPoolUtil.delete(ConstValue.RedisLock.CLOSE_ORDER_TASK_LOCK);
    }

    // @Scheduled(cron = "0 */1 * * * ?") // 表示每一分钟
    public void closeOrderTaskV1() {
        log.info("关闭订单定时任务启动{}", new Date().toString());
        int hours = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour", "2"));
        iOrderService.closeOrder(hours);
        log.info("关闭订单定时任务完成{}", new Date().toString());
    }

    // @Scheduled(cron = "0 */1 * * * ?") // 表示每一分钟
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


    @Scheduled(cron = "0 */1 * * * ?") // 表示每一分钟
    public void closeOrderTaskV3() {
        log.info("关闭订单定时任务启动{}", new Date().toString());
        Long timeOut = Long.parseLong(PropertiesUtil.getProperty("lock.timeout", "5000"));
        Long setnxTimeOut = RedisShardedPoolUtil.setNx(ConstValue.RedisLock.CLOSE_ORDER_TASK_LOCK, String.valueOf(System.currentTimeMillis() + timeOut));
        if (setnxTimeOut != null && setnxTimeOut.intValue() == 1) {
            // 返回值是 1， 表示设置成功，获取锁
            closeOrder(ConstValue.RedisLock.CLOSE_ORDER_TASK_LOCK);
        } else {
            // 双重判定
            // 未获取到锁，继续判断时间戳，看是否可以重置，并获取到锁
            String lockValueStr = RedisShardedPoolUtil.get(ConstValue.RedisLock.CLOSE_ORDER_TASK_LOCK);

            if(lockValueStr != null && System.currentTimeMillis() > Long.parseLong(lockValueStr)){
                // 表示超时了
                String getSetValueStr = RedisShardedPoolUtil.getSet(ConstValue.RedisLock.CLOSE_ORDER_TASK_LOCK, String.valueOf(System.currentTimeMillis() + timeOut));

                // 再次用当前时间戳getset
                // 返回给定的key的旧值，-> 旧值判断，是否可以获取锁
                // 当key没有旧值时，即key不存在时，返回nil -> 获取锁
                // 这里我们set了一个新的value值，获取旧的值
                if(getSetValueStr == null || StringUtils.equals(lockValueStr, getSetValueStr)){
                    closeOrder(ConstValue.RedisLock.CLOSE_ORDER_TASK_LOCK);
                }else {
                    log.info("没有获取到分布式锁{}", ConstValue.RedisLock.CLOSE_ORDER_TASK_LOCK);
                }
            }else {
                log.info("没有获取到分布式锁{}", ConstValue.RedisLock.CLOSE_ORDER_TASK_LOCK);
            }
        }
        log.info("关闭订单定时任务完成{}", new Date().toString());
    }

    private void closeOrder(String lockName)
    {
        // 为了方便测试，锁的有效期设置为50S
        // 第一重 防止死锁，设置key的有效期
        RedisShardedPoolUtil.expire(lockName, 50);
        log.info("获取{}， ThreadName：{}", lockName, Thread.currentThread().getName());
        int hours = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour", "2"));
        iOrderService.closeOrder(hours);
        RedisShardedPoolUtil.delete(lockName);
        log.info("释放{}， ThreadName：{}", lockName, Thread.currentThread().getName());
        log.info("=======================================================");
    }
}
