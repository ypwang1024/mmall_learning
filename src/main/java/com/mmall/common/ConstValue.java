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

    /**
     * Token 前缀
     */
    public static final String TOKEN_PREFIX = "token_";

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

    public interface Cart {
        /**
         * 购物车商品处于选中状态
         */
        int CHECKED = 1;
        /**
         * 购物车商品处于未选中状态
         */
        int UN_CHECKED = 0;

        /**
         * 限制失败
         */
        String LIMIT_NUM_FAIL = "LIMIT_NUM_FAIL";

        /**
         * 限制成功
         */
        String LIMIT_NUM_SUCCESS = "LIMIT_NUM_SUCCESS";
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
     * 订单状态枚举
     */
    public enum OrderStatusEnum {
        CANCELED(1, "已取消"),
        NO_PAY(10, "未支付"),
        PAID(20, "已付款"),
        SHIPPED(40, "已发货"),
        ORDER_SUCCESS(50, "订单完成"),
        ORDER_CLOSE(60, "订单关闭");

        private String value;
        private int code;

        OrderStatusEnum(int code, String value) {
            this.value = value;
            this.code = code;
        }

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }

        public static OrderStatusEnum codeOf(int code) {
            for (OrderStatusEnum orderStatusEnum : values()) {
                if (orderStatusEnum.getCode() == code) {
                    return orderStatusEnum;
                }
            }
            throw new RuntimeException("未找到对应的枚举。");
        }
    }

    /**
     * 交易状态常量，支付宝原生值
     */
    public interface AlipayCallBack {
        //	交易创建，等待买家付款
        String TRADE_STATUS_WAIT_BUYER_PAY = "WAIT_BUYER_PAY";
        // 	交易支付成功
        String TRADE_STATUS_TRADE_SUCCESS = "TRADE_SUCCESS";

        // 	未付款交易超时关闭，或支付完成后全额退款
        String TRADE_STATUS_TRADE_CLOSED = "TRADE_CLOSED";

        // 	交易结束，不可退款
        String TRADE_STATUS_TRADE_FINISHED = "TRADE_FINISHED";

        String RESPONSE_SUCCESS = "success";
        String RESPONSE_FAILED = "failed";
    }

    /**
     * 支付方式
     */
    public enum PayPlatformEnum {
        ALIPAY(1, "支付宝"),
        WECHAT(2, "微信");
        private String value;
        private int code;

        PayPlatformEnum(int code, String value) {
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
     * 支付类型
     */
    public enum PaymentTypeEnum {
        ONLINE_PAY(1, "在线支付"),
        OFFLINE_PAY(2, "货到付款");
        private String value;
        private int code;

        PaymentTypeEnum(int code, String value) {
            this.value = value;
            this.code = code;
        }

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }

        public static PaymentTypeEnum codeOf(int code) {
            // values 表示 当前枚举数组集合
            for (PaymentTypeEnum paymentTypeEnum : values()) {
                if (paymentTypeEnum.getCode() == code) {
                    return paymentTypeEnum;
                }
            }
            throw new RuntimeException("没有找到对应的枚举");
        }
    }

    /**
     * Redis缓存有效时间
     */
    public interface RedisCacheExTime {
        // 30 min
        int REDIS_SESSION_EXTIME = 60 * 30;
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

    /**
     * Redis 分布式锁
     */
    public interface RedisLock {
        // 关闭订单的分布式锁的名称
        String CLOSE_ORDER_TASK_LOCK = "CLOSE_ORDER_TASK_LOCK";
    }
}
