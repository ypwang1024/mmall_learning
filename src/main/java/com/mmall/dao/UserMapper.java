package com.mmall.dao;

import com.mmall.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    /**
     * 校验用户名是否存在
     *
     * @param username
     * @return
     */
    int checkUsername(String username);

    /**
     * 校验邮箱是否存在
     *
     * @param email
     * @return
     */
    int checkEmail(String email);

    /**
     * 查询用户
     * 多个参数时，加上mybatis参数注解，便于区分
     *
     * @return
     */
    User selectUser(@Param("username") String username, @Param("password") String password);

    /**
     * 密码找回问题
     *
     * @param username
     * @return
     */
    String selectQuestionByUsername(String username);

    /**
     * 校验问题答案
     *
     * @param username
     * @param question
     * @param answer
     * @return
     */
    int checkAnswer(@Param("username") String username, @Param("question") String question, @Param("answer") String answer);

    /**
     * 根据用户名修改密码
     *
     * @param username
     * @param paswordNew
     * @return
     */
    int updatePasswordByUsername(@Param("username") String username, @Param("paswordNew") String paswordNew);

    /**
     * 判断旧密码是否正确
     *
     * @param passwordOld
     * @param userId
     * @return
     */
    int checkPassword(@Param("passwordOld") String passwordOld, @Param("userId") Integer userId);

    /**
     * 校验email是否被其他用户占用
     *
     * @param email
     * @param userId
     * @return
     */
    int checkEmailByUserId(@Param("email") String email, @Param("userId") Integer userId);
}