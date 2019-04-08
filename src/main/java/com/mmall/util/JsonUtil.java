package com.mmall.util;

import com.google.common.collect.Lists;
import com.mmall.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @program: mmall
 * @description:使用Jackson将对象序列化成字符串
 * @author: ypwang
 * @create: 2019-04-08 21:55
 **/
@Slf4j
public class JsonUtil {
    private static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 使用静态块在类加载的时候初始化objectMapper
     */
    static {
        // 对象的所有字段全部列入
        objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.ALWAYS);

        // 取消默认转换timestamps形式
        objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);

        // 忽略空Bean转Json的错误
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);

        // 所有日期格式都统一为以下的样式，即yyyy-MM-dd HH:mm:ss
        objectMapper.setDateFormat(new SimpleDateFormat(DateTimeUtil.STANDARD_FORMAT));

        // 忽略在json字符串中存在，但是在java对象中不存在对应属性的情况，防止出现错误
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * 将对象转换成String
     *
     * @param obj 对象Obj
     * @param <T> 将该声明为泛型方法
     * @return 序列化后的String
     */
    public static <T> String obj2String(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.warn("Parse object to String error", e);
            return null;
        }
    }

    /**
     * 将对象转换成格式化后String
     *
     * @param obj 对象Obj
     * @param <T> 将该声明为泛型方法
     * @return 序列化并格式化后的String
     */
    public static <T> String obj2StringPretty(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            log.warn("Parse object to Pretty String error", e);
            return null;
        }
    }

    /**
     * 将字符串反序列化成对象
     *
     * @param str   字符串String
     * @param clazz 要转换的类Obj.Class
     * @param <T>   泛型声明
     * @return 反序列化后对象
     */
    public static <T> T string2Obj(String str, Class<T> clazz) {
        if (StringUtils.isEmpty(str) || clazz == null) {
            return null;
        }
        try {
            return clazz.equals(String.class) ? (T) str : objectMapper.readValue(str, clazz);
        } catch (Exception e) {
            log.warn("Parse String to Object error", e);
            return null;
        }
    }

    /**
     * 将字符串反序列化成引用类型对象，如List, Map等
     *
     * @param str           字符串String
     * @param typeReference 要转换的引用类
     * @param <T>           泛型声明
     * @return 引用类
     */
    public static <T> T string2Obj(String str, TypeReference typeReference) {
        if (StringUtils.isEmpty(str) || typeReference == null) {
            return null;
        }
        try {
            return typeReference.getType().equals(String.class) ? (T) str : objectMapper.readValue(str, typeReference);
        } catch (Exception e) {
            log.warn("Parse String to TypeReference Object error", e);
            return null;
        }
    }

    /**
     * 重点理解这个方法， 将Json字符串转换成集合类型的对象, 如List<User>
     *
     * @param str             Json字符串
     * @param collectionClass 集合类型Class eg: List.Class
     * @param elementClasses  集合元素类型Class: User.Class, 三个点表示不定参数
     * @param <T>             泛型声明
     * @return 转换后的集合类型的对象,
     */
    public static <T> T string2Obj(String str, Class<?> collectionClass, Class<?>... elementClasses) {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
        try {
            return objectMapper.readValue(str, javaType);
        } catch (Exception e) {
            log.warn("Parse String to Class<?>  Object error", e);
            return null;
        }
    }

    /**
     * 测试方法
     *
     * @param args
     */
    public static void main(String[] args) {
        User user = new User();
        user.setId(666);
        user.setEmail("ypwang1024@163.com");

        User user4 = new User();
        user4.setId(777);
        user4.setEmail("ypwang10247@163.com");

        String userJson = JsonUtil.obj2String(user);
        String userPrettyJson = JsonUtil.obj2StringPretty(user);

        log.info("userJson: {}", userJson);
        log.info("userPrettyJson: {}", userPrettyJson);

        User user2 = JsonUtil.string2Obj(userJson, User.class);

        User user3 = JsonUtil.string2Obj(userPrettyJson, User.class);

        List<User> userList = Lists.newArrayList();

        userList.add(user);
        userList.add(user4);

        String userListStr = JsonUtil.obj2StringPretty(userList);

        log.info("=============================");

        log.info("userListStr: {}", userListStr);

        // 转换成了LinkedHashMap, 与预想不符合
        List<User> deUserList = JsonUtil.string2Obj(userListStr, List.class);

        // TypeReference是一个接口，可以空实现
        List<User> deUserList2 = JsonUtil.string2Obj(userListStr, new TypeReference<List<User>>() {
        });

        // 另一种写法也OK
        List<User> deUserList3 = JsonUtil.string2Obj(userListStr, List.class, User.class);
    }

}
