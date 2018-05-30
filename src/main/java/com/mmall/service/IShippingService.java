package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Shipping;

import java.util.Map;

/**
 * @program: mmall
 * @description: 收货地址接口
 * @author: Ypwang1024
 * @create: 2018-05-30 07:58
 **/
public interface IShippingService {

    ServerResponse<Map> addShipping(Integer userId, Shipping shipping);

    ServerResponse<String> deleteShipping(Integer userId, Integer shippingId);

    ServerResponse updateShipping(Integer userId, Shipping shipping);

    ServerResponse<Shipping> selectShipping(Integer userId, Integer shippingId);

    ServerResponse<PageInfo> shippingList(Integer userId, Integer pageNum, Integer pageSize);
}
