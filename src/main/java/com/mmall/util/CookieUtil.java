package com.mmall.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: mmall
 * @description: Cookie 工具类
 * @author: ypwang
 * @create: 2019-04-18 20:57
 **/
@Slf4j
public class CookieUtil {

    /**
     * 域名
     */
    private final static String COOKIE_DOMAIN = ".ypwang.com";

    /**
     * cookie name
     */
    private final static String COOKIE_NAME = "mmall_login_token";

    /**
     * X:domain=".ypwang.com"
     * a:domain="A.ypwang.com"              cookie:domain=A.ypwang.com;path="/"
     * b:domain="B.ypwang.com"              cookie:domain=B.ypwang.com;path="/"
     * c:domain="A.ypwang.com/test/cc"      cookie:domain=A.ypwang.com;path="/test/cc"
     * d:domain="A.ypwang.com/test/dd"      cookie:domain=A.ypwang.com;path="/test/dd"
     * e:domain="A.ypwang.com/test"         cookie:domain=A.ypwang.com;path="/test"
     * abcde 可以拿到x 的cookie
     * ab之间互相拿不到cookie
     * cd之间互相拿不到cookie
     * cd也可以e和a的cookie
     */
    /**
     * 添加cookie
     * @param response response
     * @param token token
     */
    public static void writeLoginToken(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie(COOKIE_NAME, token);
        cookie.setDomain(COOKIE_DOMAIN);
        // 代表设置在根目录下面
        cookie.setPath("/");
        // 增加cookie的安全性，禁止被脚本获取
        cookie.setHttpOnly(true);
        // 单位是秒，如果这个maxage不设置的话，cookie就不会写入硬盘，而是写在内存，只在当前页面有效
        // -1 表示永久，60 * 60 * 24 * 365 表示一年
        cookie.setMaxAge(60 * 60 * 24 * 365);
        log.info("write cookiename:{} ,cookievalue:{}", cookie.getName(), cookie.getValue());
        response.addCookie(cookie);
    }

    /**
     * 读取cookie
     * @param request request
     * @return
     */
    public static String readLoginToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                log.info("read cookiename:{} ,cookievalue:{}", cookie.getName(), cookie.getValue());
                if (StringUtils.equals(cookie.getName(), COOKIE_NAME)) {
                    log.info("return cookiename:{} ,cookievalue:{}", cookie.getName(), cookie.getValue());
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * 删除cookie
     * @param request request
     * @param response response
     */
    public static void delLoginToken(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (StringUtils.equals(cookie.getName(), COOKIE_NAME)) {
                    cookie.setDomain(COOKIE_DOMAIN);
                    cookie.setPath("/");
                    // 设置成0，代表删除此cookie
                    cookie.setMaxAge(0);
                    log.info("del cookiename:{} ,cookievalue:{}", cookie.getName(), cookie.getValue());
                    response.addCookie(cookie);
                    return;
                }
            }
        }
    }
}
