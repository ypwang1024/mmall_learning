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

    /**
     * FTP服务器IP
     */
    public static final String FTPSERVERIP = "ftp.server.ip";

    public static final String FTPSERVERIPDEFAULTVALUE = "192.168.0.250";
    /**
     * FTP用户
     */
    public static final String FTPUSER = "ftp.user";
    /**
     * FTP用户密码
     */
    public static final String FTPPASS = "ftp.pass";
    /**
     * FTP HTTP地址前缀
     */
    public static final String FTPPREFIX = "ftp.server.http.prefix";
    /**
     * 支付宝回传地址
     */
    public static final String ALIPAYCALLBACKURL = "alipay.callback.url";
    /**
     * 密码盐值
     */
    public static final String PASSWORDSALT = "password.salt";
}
