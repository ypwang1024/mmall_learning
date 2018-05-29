package com.mmall.controller.portal;

import com.mmall.common.ConstValue;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICartService;
import com.mmall.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @program: mmall
 * @description: 购物车功能开发
 * @author: ypwang
 * @create: 2018-05-24 21:46
 **/
@Controller
@RequestMapping("/cart/")
public class CartController {

    @Autowired
    private ICartService iCartService;

    /**
     * 添加购物车
     *
     * @param session
     * @param count
     * @param productId
     * @return
     */
    @RequestMapping(value = "add_cart.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<CartVo> addIntoCart(HttpSession session, Integer count, Integer productId) {
        User user = (User) session.getAttribute(ConstValue.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.addCart(user.getId(), productId, count);
    }

    /**
     * 更新购物车
     *
     * @param session
     * @param count
     * @param productId
     * @return
     */
    @RequestMapping(value = "update_cart.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<CartVo> updateCart(HttpSession session, Integer count, Integer productId) {
        User user = (User) session.getAttribute(ConstValue.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.updateCart(user.getId(), productId, count);
    }

    /**
     * 删除购物车
     *
     * @param session
     * @param productIds
     * @return
     */
    @RequestMapping(value = "delete_cart.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<CartVo> deleteCart(HttpSession session, String productIds) {
        User user = (User) session.getAttribute(ConstValue.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.deleteCart(user.getId(), productIds);
    }

    /**
     * 获取购物车商品集合
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "cart_list.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<CartVo> cartList(HttpSession session) {
        User user = (User) session.getAttribute(ConstValue.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.cartList(user.getId());
    }

    /**
     * 全选
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "select_all.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<CartVo> selectAll(HttpSession session) {
        User user = (User) session.getAttribute(ConstValue.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.selectOrUnSelect(user.getId(), null, ConstValue.Cart.CHECKED);
    }

    /**
     * 全反选
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "un_select_all.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<CartVo> unSelectAll(HttpSession session) {
        User user = (User) session.getAttribute(ConstValue.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.selectOrUnSelect(user.getId(), null, ConstValue.Cart.UN_CHECKED);
    }

    /**
     * 单选
     *
     * @param session
     * @param productId
     * @return
     */
    @RequestMapping(value = "select_single.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<CartVo> selectSingle(HttpSession session, Integer productId) {
        User user = (User) session.getAttribute(ConstValue.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.selectOrUnSelect(user.getId(), productId, ConstValue.Cart.CHECKED);
    }

    /**
     * 单反选
     *
     * @param session
     * @param productId
     * @return
     */
    @RequestMapping(value = "unSelect_single.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<CartVo> unSelectSingle(HttpSession session, Integer productId) {
        User user = (User) session.getAttribute(ConstValue.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.selectOrUnSelect(user.getId(), productId, ConstValue.Cart.UN_CHECKED);
    }

    /**
     * 查询当前用户的购物车里面的产品数量，如果这个产品有10个，那么数量就是10
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "get_cart_product_count.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<Integer> getCartProductCount(HttpSession session) {
        User user = (User) session.getAttribute(ConstValue.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createBySuccessData(0);
        }

        return iCartService.getCartProductCount(user.getId());
    }
}
