package com.mmall.common;

import com.mmall.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @program: mmall
 * @description: Redisson 框架 工具类
 * @author: ypwang
 * @create: 2019-06-03 23:52
 **/
@Component
@Slf4j
public class RedissonManager {
    private Config config = new Config();

    private Redisson redisson = null;

    /**
     * 获取Redisson对象
     *
     * @return Redisson对象
     */
    public Redisson getRedisson() {
        return redisson;
    }

    /**
     * redis1 ip
     */
    private static String redisIp = PropertiesUtil.getProperty("redis.ip");

    /**
     * redis1 port
     */
    private static Integer redisPort = Integer.parseInt(PropertiesUtil.getProperty("redis.port"));

    /**
     * redis1 password
     */
    private static String redisPwd = PropertiesUtil.getProperty("redis.pwd");

    /**
     * redis2 ip
     */
    private static String redis2Ip = PropertiesUtil.getProperty("redis2.ip");

    /**
     * redis2 port
     */
    private static Integer redis2Port = Integer.parseInt(PropertiesUtil.getProperty("redis2.port"));

    /**
     * redis2 password
     */
    private static String redis2Pwd = PropertiesUtil.getProperty("redis2.pwd");

    /**
     * redis3 ip
     */
    private static String redis3Ip = PropertiesUtil.getProperty("redis3.ip");

    /**
     * redis3 port
     */
    private static Integer redis3Port = Integer.parseInt(PropertiesUtil.getProperty("redis3.port"));

    /**
     * redis3 password
     */
    private static String redis3Pwd = PropertiesUtil.getProperty("redis3.pwd");

    // 在类编译的时候执行，一种方法是使用静态块，这里换一种使用PostConstruct注解
    @PostConstruct
    private void init() {
        try {
            // ClusterServersConfig clusterConfigs = config.useClusterServers().addNodeAddress(redisIp + ":" + redisPort, redis2Ip + ":" + redis2Port);
            config.useSingleServer().setAddress(redisIp + ":" + redisPort).setPassword(redisPwd);
            redisson = (Redisson) Redisson.create(config);
            log.info("初始化Redisson结束");
        } catch (Exception err) {
            log.error("Redisson init err", err);
        }
    }
}
