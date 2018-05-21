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
     *
     * @param pageNum  当前页数
     * @param pageSize 每页最大记录数
     * @return
     */
    ServerResponse<PageInfo> getProductList(Integer pageNum, Integer pageSize);

    /**
     * 商品查询
     *
     * @param productName 商品名称
     * @param productId   商品ID
     * @param pageNum     当前页数
     * @param pageSize    每页最大记录数
     * @return
     */
    ServerResponse<PageInfo> searchProduct(String productName, Integer productId,
                                           Integer pageNum, Integer pageSize);

    /**
     * 前台页面获取商品信息
     *
     * @param productId
     * @return
     */
    ServerResponse<ProductDetailVo> getProductDetail(Integer productId);

    /**
     * 根据关键字和分类id获得商品信息
     *
     * @param keyword    关键字
     * @param categoryId 分类ID
     * @param pageNum    当前页码
     * @param pageSize   每页最大数
     * @param orderBy    排序依据 形如“price_desc”
     * @return
     */
    ServerResponse<PageInfo> getProductByKeyWordCategory(String keyword, Integer categoryId, int pageNum, int pageSize, String orderBy);
}
