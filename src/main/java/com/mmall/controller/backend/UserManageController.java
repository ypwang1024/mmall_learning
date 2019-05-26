package com.mmall.controller.backend;

import com.mmall.common.ConstValue;
import com.mmall.common.RedisShardedPool;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisShardedPoolUtil;
import com.sun.deploy.net.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @program: mmall
 * @description:
 * @author: ypwang
 * @create: 2018-04-16 20:03
 **/
@Controller
@RequestMapping("/manage/user")
public class UserManageController {

    @Autowired
    private IUserService iUserService;

    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session, HttpServletResponse servletResponse) {
        ServerResponse<User> response = iUserService.login(username, password);
        if (response.isSuccess()) {
            User user = response.getData();
            if (user.getRole() == ConstValue.Role.ROLE_ADMIN) {
                // 说明登录的是管理员
                // session.setAttribute(ConstValue.CURRENT_USER, user);
                // 新增 redis共享cookie session的方式
                CookieUtil.writeLoginToken(servletResponse, session.getId());
                RedisShardedPoolUtil.setEx(session.getId(), JsonUtil.obj2String(response), ConstValue.RedisCacheExTime.REDIS_SESSION_EXTIME);
                return response;
            } else {
                return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), "不是管理员，无法登录");
            }
        }
        return response;
    }
}
