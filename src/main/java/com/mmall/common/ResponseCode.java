package com.mmall.common;

/**
 * @program: mmall
 * @description: 响应枚举定义类
 * @author: ypwang
 * @create: 2018-04-13 22:15
 **/
public enum ResponseCode {
    /**成功*/
    SUCCESS(0, "SUCCESS"),
    /**错误*/
    ERROR(1, "ERROR"),
    /**需要登录*/
    NEED_LOGIN(10, "NEED_LOGIN"),
    /**非法登录*/
    ILLEGAL_ARGUMENT(2, "ILLEGAL_ARGUMENT");

    private final int code;
    private final String desc;

    ResponseCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
