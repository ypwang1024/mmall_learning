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
     *
     * @param username 账户
     * @param password 密码
     * @return 登陆信息
     */
    ServerResponse<User> login(String username, String password);

    /**
     * 账户注册
     *
     * @param user
     * @return
     */
    ServerResponse<String> register(User user);

    /**
     * 检查用户名和邮箱是否正确
     *
     * @param str
     * @param type
     * @return
     */
    ServerResponse<String> checkValid(String str, String type);

    /**
     * 查询密码找回的问题
     *
     * @param username
     * @return
     */
    ServerResponse<String> selectQuestion(String username);

    /**
     * 校验问题答案
     *
     * @param username
     * @param question
     * @param answer
     * @return
     */
    ServerResponse<String> checkAnswer(String username, String question, String answer);

    /**
     * 忘记密码时重置密码
     *
     * @param username
     * @param passwordNew
     * @param forgetToken
     * @return
     */
    ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken);

    /**
     * 登录状态下的修改密码
     *
     * @param passwordOld
     * @param passwordNew
     * @param user
     * @return
     */
    ServerResponse<String> resetPassword(String passwordOld, String passwordNew, User user);

    /**
     * 更新用户个人信息
     *
     * @param user
     * @return
     */
    ServerResponse<User> updateUserInfomation(User user);

    /**
     * 得到用户详细信息
     *
     * @param userId
     * @return
     */
    ServerResponse<User> getUserAllInfomation(Integer userId);
}
