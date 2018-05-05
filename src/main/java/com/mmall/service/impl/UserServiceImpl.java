package com.mmall.service.impl;

import com.mmall.common.ConstValue;
import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @program: mmall
 * @description: 用户接口的实现类
 * @author: ypwang
 * @create: 2018-04-13 22:14
 **/
@Service("iUserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String username, String password) {
        int resultCount = userMapper.checkUsername(username);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("用户名不存在!");
        }

        //密码登录MD5
        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectUser(username, md5Password);
        if (user == null) {
            return ServerResponse.createByErrorMessage("密码错误");
        }

        // 登录成功,处理密码
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccessMessageData("登录成功", user);
    }

    /**
     * 账户注册
     *
     * @param user
     * @return
     */
    @Override
    public ServerResponse<String> register(User user) {
        ServerResponse validResponse = this.checkValid(user.getUsername(), ConstValue.USERNAME);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }

        validResponse = this.checkValid(user.getEmail(), ConstValue.EMAIL);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }

        user.setRole(ConstValue.Role.ROLE_CUSTOMER);
        // 对密码进行MD5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int resultCount = userMapper.insert(user);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("注冊失败");
        }
        return ServerResponse.createBySuccessMessage("注册成功");
    }

    @Override
    public ServerResponse<String> checkValid(String str, String type) {
        if (StringUtils.isNotBlank(type)) {
            // 开始校验
            if (ConstValue.USERNAME.equals(type)) {
                int resultCount = userMapper.checkUsername(str);
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMessage("用户名已存在!");
                }
            }
            if (ConstValue.EMAIL.equals(type)) {
                int resultCount = userMapper.checkEmail(str);
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMessage("邮箱已存在!");
                }
            }
        } else {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        return ServerResponse.createBySuccessMessage("校验成功");
    }

    @Override
    public ServerResponse<String> selectQuestion(String username) {
        // 查询账户是否已经注册
        ServerResponse validResponse = this.checkValid(username, ConstValue.USERNAME);
        if (validResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        String question = userMapper.selectQuestionByUsername(username);
        if (StringUtils.isNotBlank(question)) {
            return ServerResponse.createBySuccessMessage(question);
        }
        return ServerResponse.createByErrorMessage("该用户尚未设置密码找回问题。");
    }

    /**
     * 校验问题答案
     *
     * @param username
     * @param question
     * @param answer
     * @return
     */
    @Override
    public ServerResponse<String> checkAnswer(String username, String question, String answer) {
        int resultCount = userMapper.checkAnswer(username, question, answer);
        if (resultCount > 0) {
            // 说明用户，问题，答案都匹配上了
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX + username, forgetToken);
            return ServerResponse.createBySuccessData(forgetToken);
        }
        return ServerResponse.createByErrorMessage("问题答案错误");
    }

    @Override
    public ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken) {
        // 1. 判断token是否为空
        if (StringUtils.isBlank(forgetToken)) {
            return ServerResponse.createByErrorMessage("参数错误，token需要传递。");
        }
        // 2. 判断username是否存在
        ServerResponse validResponse = this.checkValid(username, ConstValue.USERNAME);
        if (validResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户不存在");
        }

        // 3. 得到TOKEN
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX + username);
        if (StringUtils.isBlank(token)) {
            return ServerResponse.createByErrorMessage("token无效或者过期。");
        }

        // 4. token比对
        if (StringUtils.equals(forgetToken, token)) {
            String md5Password = MD5Util.MD5EncodeUtf8(passwordNew);
            int rowCount = userMapper.updatePasswordByUsername(username, md5Password);
            if (rowCount > 0) {
                return ServerResponse.createBySuccessMessage("修改密码成功");
            }
        } else {
            return ServerResponse.createByErrorMessage("token错误，请重新获取重置密码的token");
        }
        return ServerResponse.createByErrorMessage("修改密码失败");
    }

    @Override
    public ServerResponse<String> resetPassword(String passwordOld, String passwordNew, User user) {
        // 防止横向越权，要校验一下这个用户的旧密码，一定要指定这个用户，因为我们会查询一个count(1),如果不指定ID，那么结果count(1)很大几率大于0
        // 1. 验证旧密码是否正确
        int resultCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld), user.getId());
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("旧密码错误，请重新输入");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if (updateCount > 0) {
            return ServerResponse.createBySuccessMessage("密码修改成功");
        }
        return ServerResponse.createByErrorMessage("密码更新失败");
    }

    /**
     * 更新用户个人信息
     *
     * @param user
     * @return
     */
    @Override
    public ServerResponse<User> updateUserInfomation(User user) {
        // 校验email是否被其他用户占用
        int resultCount = userMapper.checkEmailByUserId(user.getEmail(), user.getId());
        if (resultCount > 0) {
            return ServerResponse.createByErrorMessage("该email已被其他用户使用，请更换一个email,再尝试更新");
        }

        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());

        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if (updateCount > 0) {
            updateUser = userMapper.selectByPrimaryKey(user.getId());
            updateUser.setPassword("");
            return ServerResponse.createBySuccessMessageData("更新更新个人信息成功", updateUser);
        }
        return ServerResponse.createByErrorMessage("更新个人信息失败");
    }

    /**
     * 得到用户详细信息
     *
     * @param userId
     * @return
     */
    @Override
    public ServerResponse<User> getUserAllInfomation(Integer userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) {
            return ServerResponse.createByErrorMessage("找不到当前用户");
        }
        // 对密码进行清空
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccessData(user);
    }

    /**
     * 判断用户是否是管理员
     *
     * @param user
     * @return
     */
    @Override
    public ServerResponse checkAdminRole(User user) {

        if (user != null && user.getRole().intValue() == ConstValue.Role.ROLE_ADMIN) {
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }
}
