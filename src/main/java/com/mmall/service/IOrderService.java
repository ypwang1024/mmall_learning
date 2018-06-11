package com.mmall.service;

import com.mmall.common.ServerResponse;

/**
 * @program: mmall
 * @description: 订单接口
 * @author: ypwang
 * @create: 2018-06-11 07:56
 **/
public interface IOrderService {

    ServerResponse pay(Long orderNo, Integer userId, String path);
}
