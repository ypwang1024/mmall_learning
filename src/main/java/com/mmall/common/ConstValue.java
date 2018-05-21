package com.mmall.common;

import com.google.common.collect.Sets;

import java.util.Set;

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

    public interface ProductListOrderBy {
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_desc", "price_asc");
    }

    public enum ProductStatusEnum {

        ON_SALE("在线", 1);

        private String value;
        private int code;

        ProductStatusEnum(String value, int code) {
            this.value = value;
            this.code = code;
        }

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }

    }

    /**
     * FTP服务器IP
     */
    public static final String FTPSERVERIP = "ftp.server.ip";

    public static final String FTPSERVERIPPORT = "ftp.server.port";

    public static final String FTPSERVERIPDEFAULTVALUE = "192.168.1.104";

    public static final String FTPSERVERPORTDEFAULTVALUE = "21";
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
