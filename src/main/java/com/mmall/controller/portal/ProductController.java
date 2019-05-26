package com.mmall.controller.portal;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.service.IProductService;
import com.mmall.vo.ProductDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @program: mmall
 * @description:
 * @author: ypwang
 * @create: 2018-05-20 21:00
 **/
@Controller()
@RequestMapping("/product/")
public class ProductController {

    @Autowired
    private IProductService iProductService;

    /**
     * 前端获取商品信息
     *
     * @param productId
     * @return
     */
    @RequestMapping(value = "product_detail.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<ProductDetailVo> productDtail(Integer productId) {
        return iProductService.getProductDetail(productId);
    }

    /*
    * 使用RESTful获取商品详情
    * */
    @RequestMapping(value = "{productId}", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<ProductDetailVo> productDtailRESTful(@PathVariable Integer productId) {
        return iProductService.getProductDetail(productId);
    }

    @RequestMapping(value = "product_list.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<PageInfo> productList(@RequestParam(value = "keyword", required = false) String keyword,
                                                @RequestParam(value = "categoryId", required = false) Integer categoryId,
                                                @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                @RequestParam(value = "orderBy", defaultValue = "") String orderBy) {
        return iProductService.getProductByKeyWordCategory(keyword, categoryId, pageNum, pageSize, orderBy);
    }

    // http://www.ypwang.com/product/%E6%89%8B%E6%9C%BA/100012/1/10/price_asc 各参数取值不能为空
    @RequestMapping(value = "{keyword}/{categoryId}/{pageNum}/{pageSize}/{orderBy}", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<PageInfo> productListRESTful(@PathVariable(value = "keyword") String keyword,
                                                @PathVariable(value = "categoryId") Integer categoryId,
                                                @PathVariable(value = "pageNum") int pageNum,
                                                @PathVariable(value = "pageSize") int pageSize,
                                                @PathVariable(value = "orderBy") String orderBy) {
        return iProductService.getProductByKeyWordCategory(keyword, categoryId, pageNum, pageSize, orderBy);
    }

    // http://www.ypwang.com/product/keyword/手机/1/10/price_asc
    @RequestMapping(value = "keyword/{keyword}/{pageNum}/{pageSize}/{orderBy}", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<PageInfo> productListRESTful(@PathVariable(value = "keyword") String keyword,
                                                       @PathVariable(value = "pageNum") int pageNum,
                                                       @PathVariable(value = "pageSize") int pageSize,
                                                       @PathVariable(value = "orderBy") String orderBy) {
        return iProductService.getProductByKeyWordCategory(keyword, null, pageNum, pageSize, orderBy);
    }

    // http://www.ypwang.com/product/category/100012/1/10/price_asc
    @RequestMapping(value = "category/{categoryId}/{pageNum}/{pageSize}/{orderBy}", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<PageInfo> productListRESTful(@PathVariable(value = "categoryId") Integer categoryId,
                                                       @PathVariable(value = "pageNum") int pageNum,
                                                       @PathVariable(value = "pageSize") int pageSize,
                                                       @PathVariable(value = "orderBy") String orderBy) {
        return iProductService.getProductByKeyWordCategory("", categoryId, pageNum, pageSize, orderBy);
    }
}
