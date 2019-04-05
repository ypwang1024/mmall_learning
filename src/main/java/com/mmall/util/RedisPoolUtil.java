package com.mmall.util;

import com.mmall.common.RedisPool;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

/**
 * @program: mmall
 * @description: 封装Redis API
 * @author: ypwang
 * @create: 2019-04-05 10:32
 **/
@Slf4j
public class RedisPoolUtil {

    /**
     * 设值
     *
     * @param key   key
     * @param value value
     * @return 受影响行数
     */
    public static String set(String key, String value) {
        Jedis jedis = null;
        String result = null;

        try {
            jedis = RedisPool.getJedis();
            jedis.set(key, value);
        } catch (Exception e) {
            log.error("set key:{} value:{} error", key, value, e);
            RedisPool.returnBrokenResource(jedis);
            return result;
        }
        RedisPool.returnResource(jedis);
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
        Jedis jedis = null;
        String result = null;

        try {
            jedis = RedisPool.getJedis();
            jedis.setex(key, exTime, value);
        } catch (Exception e) {
            log.error("setex key:{} value:{} extime:{} error", key, value, exTime, e);
            RedisPool.returnBrokenResource(jedis);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }

    /**
     * 取值
     *
     * @param key key
     * @return value
     */
    public static String get(String key) {
        Jedis jedis = null;
        String result = null;

        try {
            jedis = RedisPool.getJedis();
            result = jedis.get(key);
        } catch (Exception e) {
            log.error("get key:{} error", key, e);
            RedisPool.returnBrokenResource(jedis);
            return result;
        }
        RedisPool.returnResource(jedis);
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
        Jedis jedis = null;
        Long result = null;

        try {
            jedis = RedisPool.getJedis();
            result = jedis.expire(key, exTime);
        } catch (Exception e) {
            log.error("expire key:{} exTime:{} error", key, exTime, e);
            RedisPool.returnBrokenResource(jedis);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }

    /**
     * 删除
     *
     * @param key key
     * @return 受影响行数
     */
    public static Long delete(String key) {
        Jedis jedis = null;
        Long result = null;

        try {
            jedis = RedisPool.getJedis();
            result = jedis.del(key);
        } catch (Exception e) {
            log.error("del key:{} error", key, e);
            RedisPool.returnBrokenResource(jedis);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }

    public static void main(String[] args) {
        Jedis jedis = RedisPool.getJedis();
        RedisPoolUtil.set("keyTest", "value");

        String value = RedisPoolUtil.get("keyTest");
        RedisPoolUtil.setEx("keyTest1", "value", 60*10);

        RedisPoolUtil.expire("keyTest", 60*20);

        RedisPoolUtil.set("keyTest2", "value");

        RedisPoolUtil.delete("keyTest2");
    }
}
