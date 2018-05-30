package com.mmall.controller.portal;

import com.mmall.common.ConstValue;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Shipping;
import com.mmall.pojo.User;
import com.mmall.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @program: mmall
 * @description: 收货地址Controller
 * @author: ypwang
 * @create: 2018-05-30 07:57
 **/
@Controller
@RequestMapping("/shipping/")
public class ShippingController {

    @Autowired
    private IShippingService iShippingService;

    /**
     * 添加收货地址， 使用springMvc 中对象数据绑定 shipping
     *
     * @param session
     * @param shipping
     * @return
     */
    @RequestMapping(value = "add_shipping.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<Map> addShipping(HttpSession session, Shipping shipping) {
        User user = (User) session.getAttribute(ConstValue.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iShippingService.addShipping(user.getId(), shipping);
    }

    /**
     * 删除收货地址
     *
     * @param session
     * @param shippingId
     * @return
     */
    @RequestMapping(value = "delete_shipping.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> deleteShipping(HttpSession session, Integer shippingId) {
        User user = (User) session.getAttribute(ConstValue.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iShippingService.deleteShipping(user.getId(), shippingId);
    }
}
