package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.vo.CartVo;

/**
 * @program: mmall
 * @description: 购物车接口
 * @author: Ypwang1024
 * @create: 2018-05-24 21:53
 **/
public interface ICartService {
    ServerResponse<CartVo> addCart(Integer userId, Integer productId, Integer count);

    ServerResponse<CartVo> updateCart(Integer userId, Integer productId, Integer count);

    ServerResponse<CartVo> deleteCart(Integer userId, String productIds);

    ServerResponse<CartVo> cartList(Integer userId);

    ServerResponse<CartVo> selectOrUnSelect(Integer userId, Integer productId, Integer checked);

    ServerResponse<Integer> getCartProductCount(Integer userId);
}
