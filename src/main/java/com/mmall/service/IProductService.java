package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;

/**
 * @program: mmall
 * @description:
 * @author: ypwang
 * @create: 2018-05-12 10:44
 **/
public interface IProductService {
    /**
     * 对产品进行更新或新增
     *
     * @param product
     * @return
     */
    ServerResponse saveOrUpdateProduct(Product product);

    /**
     * 设置商品状态
     *
     * @param productId
     * @param status
     * @return
     */
    ServerResponse<String> setSaleStatus(Integer productId, Integer status);

    /**
     * 获得商品详情
     *
     * @param productId
     * @return
     */
    ServerResponse manageProductDetail(Integer productId);
}
