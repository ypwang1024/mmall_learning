package com.mmall.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * @program: mmall
 * @description: 配置工具读取
 * @author: ypwang
 * @create: 2018-05-13 22:40
 **/
public class PropertiesUtil {
    private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);
    private static Properties properties = new Properties();

    static {
        String fileName = "mmall.properties";
        try {
            properties.load(new InputStreamReader(PropertiesUtil.class.
                    getClassLoader().getResourceAsStream(fileName), "UTF-8"));
        } catch (IOException e) {
            logger.error("配置文件读取异常", e);
        }
    }

    /**
     * 根据配置文件key值获得对应value,不存在key返回null
     *
     * @param key
     * @return
     */
    public static String getProperty(String key) {
        String value = properties.getProperty(key.trim());
        if (StringUtils.isBlank(value)) {
            return null;
        }
        return value;
    }

    /**
     * 根据配置文件key值获得对应value,不存在key返回默认值defaultValue
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getProperty(String key, String defaultValue) {
        String value = properties.getProperty(key.trim());
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }
        return value;
    }
}
