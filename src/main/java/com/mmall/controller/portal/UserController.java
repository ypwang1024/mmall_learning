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
@RequestMapping("/user/")
/**
 * @program: mmall
 * @description: 使用分布式redis实现代码入侵(需要改写代码)的单点登录解决方案
 * @author: Ypwang1024
 * @create: 2018-04-13 21:45
 **/
public class UserController {

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
            // session.setAttribute(ConstValue.CURRENT_USER, response.getData());
            CookieUtil.writeLoginToken(httpServletResponse, session.getId());
            RedisShardedPoolUtil.setEx(session.getId(), JsonUtil.obj2String(response.getData()), ConstValue.RedisCacheExTime.REDIS_SESSION_EXTIME);
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
    public ServerResponse<String> logout(HttpServletRequest request, HttpServletResponse response) {
        // session.removeAttribute(ConstValue.CURRENT_USER);
        // 先从cookie拿到缓存id
        String loginToken = CookieUtil.readLoginToken(request);
        // 删除cookie
        CookieUtil.delLoginToken(request, response);
        // 删除redis登录用户缓存
        RedisShardedPoolUtil.delete(loginToken);
        return ServerResponse.createBySuccess();
    }

    /**
     * 账户注册
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "register.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(User user) {
        return iUserService.register(user);
    }

    /**
     * 检验用户名，和邮箱是否存在
     *
     * @param str
     * @param type
     * @return
     */
    @RequestMapping(value = "check_valid.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkValid(String str, String type) {
        return iUserService.checkValid(str, type);
    }

    /**
     * 获取当前登录用户信息
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "get_user_info.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpServletRequest request) {
        // 不再从session中取登录信息
        // User user = (User) session.getAttribute(ConstValue.CURRENT_USER);
        // 从缓存中取到登录用户信息
        // 先从cookie拿到缓存id
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录，无法获取当前用户的信息");
        }

        String userJson = RedisShardedPoolUtil.get(loginToken);

        User user = JsonUtil.string2Obj(userJson, User.class);
        if (user != null) {
            return ServerResponse.createBySuccessData(user);
        }
        return ServerResponse.createByErrorMessage("用户未登录，无法获取当前用户的信息");
    }

    /**
     * 忘记密码，获取密码提示问题
     *
     * @param username
     * @return
     */
    @RequestMapping(value = "forget_get_question.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetGetQuestion(String username) {
        return iUserService.selectQuestion(username);
    }

    /**
     * 校验问题答案是否正确
     *
     * @param username
     * @param question
     * @param answer
     * @return
     */
    @RequestMapping(value = "forget_check_answer.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetCheckAnswer(String username, String question, String answer) {
        return iUserService.checkAnswer(username, question, answer);
    }

    /**
     * 忘记密码时的重置密码
     *
     * @param username
     * @param passwordNew
     * @param forgetToken
     * @return
     */
    @RequestMapping(value = "forget_reset_password.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken) {
        return iUserService.forgetResetPassword(username, passwordNew, forgetToken);
    }

    @RequestMapping(value = "online_reset_password.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> resetPassword(HttpServletRequest request, String passwordOld, String passwordNew) {
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录，无法获取当前用户的信息");
        }

        String userJson = RedisShardedPoolUtil.get(loginToken);

        User user = JsonUtil.string2Obj(userJson, User.class);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登录，请登录");
        }
        return iUserService.resetPassword(passwordOld, passwordNew, user);
    }

    /**
     * 更新用户个人信息
     *
     * @param session
     * @param user
     * @return
     */
    @RequestMapping(value = "update_user_infomation.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> updateUserInfomation(HttpSession session, HttpServletRequest request, User user) {
        // 1. 判断是否登录
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录，无法获取当前用户的信息");
        }

        String userJson = RedisShardedPoolUtil.get(loginToken);

        User currentUser = JsonUtil.string2Obj(userJson, User.class);
        if (currentUser == null) {
            return ServerResponse.createByErrorMessage("用户未登录，请登录");
        }
        // 2. 执行更新
        user.setId(currentUser.getId());
        user.setUsername(currentUser.getUsername());
        ServerResponse<User> response = iUserService.updateUserInfomation(user);
        if (response.isSuccess()) {
            // 返回值不包含username, 重新赋值
            response.getData().setUsername(currentUser.getUsername());
            // session.setAttribute(ConstValue.CURRENT_USER, response.getData());
            RedisShardedPoolUtil.setEx(session.getId(), JsonUtil.obj2String(response.getData()), ConstValue.RedisCacheExTime.REDIS_SESSION_EXTIME);
        }
        return response;
    }

    /**
     * 得到用户详细信息
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "get_user_infomation.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserAllInfomation(HttpServletRequest request) {
        // 1. 判断是否登录
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录，无法获取当前用户的信息");
        }

        String userJson = RedisShardedPoolUtil.get(loginToken);

        User currentUser = JsonUtil.string2Obj(userJson, User.class);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录，需要强制登录status=10");
        }
        return iUserService.getUserAllInfomation(currentUser.getId());
    }
}
