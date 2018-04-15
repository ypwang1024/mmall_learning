package com.mmall.common;

/**
 * @program: mmall
 * @description: 定义系统常量值
 * @author: ypwang
 * @create: 2018-04-15 20:34
 **/
public class ConstValue {

    /**
     * 当前用户
     */
    public static final String CURRENT_USER = "currentUser";

    /**
     * 邮箱类型
     */
    public static final String EMAIL = "email";

    /**
     * 用户名类型
     */
    public static final String USERNAME = "username";

    public interface Role {
        /**
         * 普通用户
         */
        int ROLE_CUSTOMER = 0;
        /**
         * 管理员
         */
        int ROLE_ADMIN = 1;
    }
}
