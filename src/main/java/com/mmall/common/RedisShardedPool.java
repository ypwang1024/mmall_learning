package com.mmall.common;

import com.google.common.collect.Lists;
import com.mmall.util.PropertiesUtil;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.util.Hashing;
import redis.clients.util.Sharded;

import java.util.List;

/**
 * @program: mmall
 * @description: Redis 分布式连接池
 * @author: ypwang
 * @create: 2019-05-06 23:44
 **/
public class RedisShardedPool {
    /**
     * Sharded Jedis连接池
     */
    // private static JedisPool pool;
    private static ShardedJedisPool pool;
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

    // TODO 如果我加减一个redis服务器不仅要修改配置，还要改代码？？
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

    private static void initPool() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);
        // 连接耗尽时是否阻塞，false 抛出异常，true阻塞直到超时，默认true
        config.setBlockWhenExhausted(true);

        // pool = new JedisPool(config, redisIp, redisPort, 1000 * 2);

        JedisShardInfo info1 = new JedisShardInfo(redisIp, redisPort, 1000 * 2);
        info1.setPassword(redisPwd);
        JedisShardInfo info2 = new JedisShardInfo(redis2Ip, redis2Port, 1000 * 2);
        info2.setPassword(redis2Pwd);
        JedisShardInfo info3 = new JedisShardInfo(redis3Ip, redis3Port, 1000 * 2);
        info3.setPassword(redis3Pwd);
        List<JedisShardInfo> jedisShardInfoList = Lists.newArrayList(info1, info2, info3);

        // config 配置信息 MURMUR_HASH 指的是consistent hashing 一致性算法（默认）。还有一个是MD5, 第四个参数指的是key的匹配模式
        pool = new ShardedJedisPool(config, jedisShardInfoList, Hashing.MURMUR_HASH, Sharded.DEFAULT_KEY_TAG_PATTERN);
    }

    static {
        // 设置静态块，在类加载的时候进行初始化，且仅初始化一次
        initPool();
    }

    /**
     * 取连接
     *
     * @return 返回一个ShardedJedis连接
     */
    public static ShardedJedis getJedis() {
        return pool.getResource();
    }

    /**
     * 释放连接
     *
     * @param shardedJedis ShardedJedis连接
     */
    public static void returnResource(ShardedJedis shardedJedis) {
        pool.returnResource(shardedJedis);
    }

    /**
     * 释放坏连接
     *
     * @param shardedJedis ShardedJedis连接
     */
    public static void returnBrokenResource(ShardedJedis shardedJedis) {
        pool.returnBrokenResource(shardedJedis);
    }

    public static void main(String[] args) {
        ShardedJedis jedis = getJedis();

        for(int i = 20; i < 30; i ++)
        {
            jedis.set("key" + i, "value" + i);
        }
        returnResource(jedis);
        System.out.println(jedis.get("key1"));
        System.out.println("program is end...");
    }
}
