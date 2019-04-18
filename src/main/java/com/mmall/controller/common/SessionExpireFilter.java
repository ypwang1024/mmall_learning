package com.mmall.controller.common;

import com.mmall.common.ConstValue;
import com.mmall.pojo.User;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisPoolUtil;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @program: mmall
 * @description: session过滤器
 * @author: ypwang
 * @create: 2019-04-18 22:59
 **/
public class SessionExpireFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        // 从cookie中取到缓存key
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isEmpty(loginToken)) {
            // 根据缓存key取到user json
            String userJson = RedisPoolUtil.get(loginToken);
            User user = JsonUtil.string2Obj(userJson, User.class);
            if (user != null) {
                // 重置redis缓存时间
                RedisPoolUtil.expire(loginToken, ConstValue.RedisCacheExTime.REDIS_SESSION_EXTIME);
            }
        }

        // 放到过滤链上。
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
