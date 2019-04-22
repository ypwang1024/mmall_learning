package com.mmall.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @program: mmall
 * @description: Token缓存, 已由Redis缓存代替，该类已废弃
 * @author: ypwang
 * @create: 2018-04-16 12:45
 **/
@Slf4j
public class TokenCache {
    // private static Logger logger = LoggerFactory.getLogger(TokenCache.class);

    public static final String TOKEN_PREFIX = "token_";
    // LRU算法
    /**
     * 设置初始容量，最大容量，有效期等,匿名实现，默认数据加载实现
     */
    private static LoadingCache<String, String> localCache = CacheBuilder.newBuilder().initialCapacity(1000)
            .maximumSize(10000).expireAfterAccess(12, TimeUnit.HOURS).build(new CacheLoader<String, String>() {
                @Override
                /**
                 * 默认数据加载实现,当调用get取值的时候，如果key没有对应的值，就调用这个方法进行加载。
                 */
                public String load(String s) throws Exception {
                    return "null";
                }
            });

    public static void setKey(String key, String value) {
        localCache.put(key, value);
    }

    public static String getKey(String key) {
        String value = null;
        try {
            value = localCache.get(key);
            if ("null".equals(value)) {
                return null;
            }
            return value;
        } catch (ExecutionException e) {
            log.error("localCache get error", e);
        }
        return null;
    }
}
