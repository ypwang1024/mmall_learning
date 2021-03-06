package com.mmall.controller.portal;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ConstValue;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Shipping;
import com.mmall.pojo.User;
import com.mmall.service.IShippingService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisShardedPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
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
    public ServerResponse<Map> addShipping(HttpServletRequest request, Shipping shipping) {
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录，无法获取当前用户的信息");
        }

        String userJson = RedisShardedPoolUtil.get(loginToken);

        User user = JsonUtil.string2Obj(userJson, User.class);
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
    public ServerResponse<String> deleteShipping(HttpServletRequest request, Integer shippingId) {
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录，无法获取当前用户的信息");
        }

        String userJson = RedisShardedPoolUtil.get(loginToken);

        User user = JsonUtil.string2Obj(userJson, User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iShippingService.deleteShipping(user.getId(), shippingId);
    }

    /**
     * 更新收货地址
     *
     * @param session
     * @param shipping
     * @return
     */
    @RequestMapping(value = "update_shipping.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> updateShipping(HttpServletRequest request, Shipping shipping) {
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录，无法获取当前用户的信息");
        }

        String userJson = RedisShardedPoolUtil.get(loginToken);

        User user = JsonUtil.string2Obj(userJson, User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iShippingService.updateShipping(user.getId(), shipping);
    }

    /**
     * 查询收货地址
     *
     * @param session
     * @param shippingId
     * @return
     */
    @RequestMapping(value = "select_shipping.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<Shipping> selectShipping(HttpServletRequest request, Integer shippingId) {
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录，无法获取当前用户的信息");
        }

        String userJson = RedisShardedPoolUtil.get(loginToken);

        User user = JsonUtil.string2Obj(userJson, User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iShippingService.selectShipping(user.getId(), shippingId);
    }

    /**
     * 查询收货地址列表
     *
     * @param session
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "shipping_list.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<PageInfo> shippingList(HttpServletRequest request,
                                                 @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                 @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录，无法获取当前用户的信息");
        }

        String userJson = RedisShardedPoolUtil.get(loginToken);

        User user = JsonUtil.string2Obj(userJson, User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iShippingService.shippingList(user.getId(), pageNum, pageSize);
    }
}
