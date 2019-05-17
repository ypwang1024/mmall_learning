package com.mmall.controller.portal;

import com.mmall.common.ConstValue;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisShardedPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
/**Url的统一上层入口*/
@RequestMapping("/user/springsession/")
/**
 * @program: mmall
 * @description: 使用spring session 实现无代码入侵（使用原生session）的单点登录解决方案
 * @author: Ypwang1024
 * @create: 2018-04-13 21:45
 **/
public class UserSpringSessionController {

    @Autowired
    private IUserService iUserService;

    /**
     * 用户登录
     *
     * @param username 账号
     * @param password 密码
     * @param session  session
     * @return 登录信息
     */
    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    /**添加ResponseBody注解，若在dispatcher-servlet.xml注解驱动中配置了mvc:message-converters
     * 及其application/json;charset=UTF-8 将会自动序列化JSON
     * */
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session, HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest) {
        ServerResponse<User> response = iUserService.login(username, password);

        if (response.isSuccess()) {
            session.setAttribute(ConstValue.CURRENT_USER, response.getData());
//            CookieUtil.writeLoginToken(httpServletResponse, session.getId());
//            RedisShardedPoolUtil.setEx(session.getId(), JsonUtil.obj2String(response.getData()), ConstValue.RedisCacheExTime.REDIS_SESSION_EXTIME);
        }
        return response;
    }

    /**
     * 用戶退出登录
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "logout.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> logout(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
         session.removeAttribute(ConstValue.CURRENT_USER);
//        // 先从cookie拿到缓存id
//        String loginToken = CookieUtil.readLoginToken(request);
//        // 删除cookie
//        CookieUtil.delLoginToken(request, response);
//        // 删除redis登录用户缓存
//        RedisShardedPoolUtil.delete(loginToken);
        return ServerResponse.createBySuccess();
    }

    /**
     * 获取当前登录用户信息
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "get_user_info.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession session, HttpServletRequest request) {
        // 从session中取登录信息
         User user = (User) session.getAttribute(ConstValue.CURRENT_USER);
//        // 从缓存中取到登录用户信息
//        // 先从cookie拿到缓存id
//        String loginToken = CookieUtil.readLoginToken(request);
//        if (StringUtils.isEmpty(loginToken)) {
//            return ServerResponse.createByErrorMessage("用户未登录，无法获取当前用户的信息");
//        }
//
//        String userJson = RedisShardedPoolUtil.get(loginToken);
//
//        User user = JsonUtil.string2Obj(userJson, User.class);
        if (user != null) {
            return ServerResponse.createBySuccessData(user);
        }
        return ServerResponse.createByErrorMessage("用户未登录，无法获取当前用户的信息");
    }
}
