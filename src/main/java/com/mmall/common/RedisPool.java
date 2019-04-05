package com.mmall.common;

import com.mmall.util.PropertiesUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @program: mmall
 * @description: Redis 工具
 * @author: ypwang
 * @create: 2019-04-03 22:55
 **/
public class RedisPool {
    /**
     * Jedis连接池
     */
    private static JedisPool pool;

    /**
     * 最大连接数
     */
    private static Integer maxTotal = Integer.parseInt(PropertiesUtil.getProperty("redis.max.total", "20"));

    /**
     * 在jedisPool中最大的idle状态（空闲的）的jedis实例的个数
     */
    private static Integer maxIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.max.idle", "10"));

    /**
     * 在jedisPool中最小的idle状态（空闲的）的jedis实例的个数
     */
    private static Integer minIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.min.idle", "2"));

    /**
     * 在borrow取出一个jedis实例的时候，是否要进行验证操作，如果赋值true,则得到的jedis实例肯定是可以用的。
     */
    private static Boolean testOnBorrow = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.borrow", "true"));

    /**
     * 在return放回一个jedis实例的时候，是否要进行验证操作，如果赋值true,则放回jedispool的jedis实例肯定是可以用的。
     */
    private static Boolean testOnReturn = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.return", "true"));

    /**
     * redis ip
     */
    private static String redisIp = PropertiesUtil.getProperty("redis.ip");

    /**
     * redis port
     */
    private static Integer redisPort = Integer.parseInt(PropertiesUtil.getProperty("redis.port"));

    private static void initPool() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);

        // 连接耗尽时是否阻塞，false 抛出异常，true阻塞直到超时，默认true
        config.setBlockWhenExhausted(true);

        pool = new JedisPool(config, redisIp, redisPort, 1000 * 2);
    }

    static {
        // 设置静态块，在类加载的时候进行初始化，且仅初始化一次
        initPool();
    }

    /**
     * 取连接
     *
     * @return 返回一个jedis连接
     */
    public static Jedis getJedis() {
        return pool.getResource();
    }

    /**
     * 释放连接
     *
     * @param jedis jedis连接
     */
    public static void returnResource(Jedis jedis) {
        pool.returnResource(jedis);
    }

    /**
     * 释放坏连接
     *
     * @param jedis jedis连接
     */
    public static void returnBrokenResource(Jedis jedis) {
        pool.returnBrokenResource(jedis);
    }

    public static void main(String[] args) {
        Jedis jedis = getJedis();
        jedis.set("test1", "test1");
        System.out.println(jedis.get("test"));
        returnResource(jedis);
    }
}
