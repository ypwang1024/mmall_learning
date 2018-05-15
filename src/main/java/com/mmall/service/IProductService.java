package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.vo.ProductDetailVo;

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
    ServerResponse<ProductDetailVo> manageProductDetail(Integer productId);

    /**
     * 使用pageHelper分页插件。实现分页
     * @param pageNum 当前页数
     * @param pageSize 每页最大记录数
     * @return
     */
    ServerResponse<PageInfo> getProductList(Integer pageNum, Integer pageSize);
}
