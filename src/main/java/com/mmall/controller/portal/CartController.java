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

    // todo 全选，全反选，单选，单反选
}
