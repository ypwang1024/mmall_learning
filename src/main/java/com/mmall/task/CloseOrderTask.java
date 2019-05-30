package com.mmall.task;

import com.mmall.service.IOrderService;
import com.mmall.util.PropertiesUtil;
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

    @Scheduled(cron = "0 */1 * * * ?") // 表示每一分钟
    public void closeOrderTaskV1()
    {
        log.info("关闭订单定时任务启动{}", new Date().toString());
        int hours = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour", "2"));
        iOrderService.closeOrder(hours);
        log.info("关闭订单定时任务完成{}", new Date().toString());
    }
}
