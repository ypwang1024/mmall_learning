package com.mmall.util;

import com.mmall.common.RedisShardedPool;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.ShardedJedis;

/**
 * @program: mmall
 * @description: 封装分布式Sharded Redis API
 * @author: ypwang
 * @create: 2019年5月7日 23:15
 **/
@Slf4j
public class RedisShardedPoolUtil {

    /**
     * 设值
     *
     * @param key   key
     * @param value value
     * @return 受影响行数
     */
    public static String set(String key, String value) {
        ShardedJedis jedis = null;
        String result = null;

        try {
            jedis = RedisShardedPool.getJedis();
            jedis.set(key, value);
        } catch (Exception e) {
            log.error("set key:{} value:{} error", key, value, e);
            RedisShardedPool.returnBrokenResource(jedis);
            return result;
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }

    /**
     * 设值，带过期时间
     *
     * @param key    key
     * @param value  value
     * @param exTime 过期时间 单位秒
     * @return 受影响行数
     */
    public static String setEx(String key, String value, Integer exTime) {
        ShardedJedis jedis = null;
        String result = null;

        try {
            jedis = RedisShardedPool.getJedis();
            jedis.setex(key, exTime, value);
        } catch (Exception e) {
            log.error("setex key:{} value:{} extime:{} error", key, value, exTime, e);
            RedisShardedPool.returnBrokenResource(jedis);
            return result;
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }

    /**
     * 取值
     *
     * @param key key
     * @return value
     */
    public static String get(String key) {
        ShardedJedis jedis = null;
        String result = null;

        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.get(key);
        } catch (Exception e) {
            log.error("get key:{} error", key, e);
            RedisShardedPool.returnBrokenResource(jedis);
            return result;
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }

    /**
     * 设置key的有效期
     *
     * @param key    key
     * @param exTime 有效期 单位秒
     * @return 1 设置成功， 0 设置失败
     */
    public static Long expire(String key, Integer exTime) {
        ShardedJedis jedis = null;
        Long result = null;

        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.expire(key, exTime);
        } catch (Exception e) {
            log.error("expire key:{} exTime:{} error", key, exTime, e);
            RedisShardedPool.returnBrokenResource(jedis);
            return result;
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }

    /**
     * 删除
     *
     * @param key key
     * @return 受影响行数
     */
    public static Long delete(String key) {
        ShardedJedis jedis = null;
        Long result = null;

        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.del(key);
        } catch (Exception e) {
            log.error("del key:{} error", key, e);
            RedisShardedPool.returnBrokenResource(jedis);
            return result;
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }

    public static void main(String[] args) {
        ShardedJedis jedis = RedisShardedPool.getJedis();
        RedisShardedPoolUtil.set("keyTest", "value");

        String value = RedisShardedPoolUtil.get("keyTest");
        RedisShardedPoolUtil.setEx("keyTest1", "value", 60*10);

        RedisShardedPoolUtil.expire("keyTest", 60*20);

        RedisShardedPoolUtil.set("keyTest2", "value");

        RedisShardedPoolUtil.delete("keyTest2");
    }
}
