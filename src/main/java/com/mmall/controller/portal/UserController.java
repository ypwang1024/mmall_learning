package com.mmall.controller.portal;

import com.mmall.common.ConstValue;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
/**Url的统一上层入口*/
@RequestMapping("/user/")
/**
 * @program: mmall
 * @description:
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
    public ServerResponse<User> login(String username, String password, HttpSession session) {
        ServerResponse<User> response = iUserService.login(username, password);

        if (response.isSuccess()) {
            session.setAttribute(ConstValue.CURRENT_USER, response.getData());
        }
        return response;
    }

    /**
     * 用戶退出登录
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "logout.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> logout(HttpSession session) {
        session.removeAttribute(ConstValue.CURRENT_USER);
        return ServerResponse.createBySuccess();
    }

    /**
     * 账户注册
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "register.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> register(User user) {
        return iUserService.register(user);
    }

    @RequestMapping(value = "check_valid.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> checkValid(String str, String type) {
        return iUserService.checkValid(str, type);
    }
}
