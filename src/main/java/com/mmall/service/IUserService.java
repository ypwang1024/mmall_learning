package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;

/**
 * @program: mmall
 * @description: 用户服务层接口
 * @author: Ypwang1024
 * @create: 2018-04-13 22:10
 **/
public interface IUserService {
    /**
     * 用户登陆接口
     * @param username 账户
     * @param password 密码
     * @return 登陆信息
     */
    ServerResponse<User> login(String username, String password);

    /**
     * 账户注册
     * @param user
     * @return
     */
    ServerResponse<String> register(User user);

    /**
     * 检查用户名和邮箱是否正确
     * @param str
     * @param type
     * @return
     */
    ServerResponse<String> checkValid(String str, String type);
}
