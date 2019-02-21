package com.mmall.controller.portal;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.google.common.collect.Maps;
import com.mmall.common.ConstValue;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.OrderItemMapper;
import com.mmall.pojo.User;
import com.mmall.service.IOrderService;
import com.mmall.vo.OrderVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.Map;

/**
 * @program: mmall
 * @description: 订单功能开发 用于和支付宝对接
 * @author: ypwang
 * @create: 2018-06-11 07:53
 **/

@Controller
@RequestMapping("/order/")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private IOrderService iOrderService;

    /**
     * 支付宝支付
     *
     * @param session
     * @param orderNo
     * @param request
     * @return
     */
    @RequestMapping(value = "pay.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse pay(HttpSession session, Long orderNo, HttpServletRequest request) {
        User user = (User) session.getAttribute(ConstValue.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        // 得到当前tomcat服务路径
        String path = request.getSession().getServletContext().getRealPath("upload");
        return ServerResponse.createBySuccessData(iOrderService.pay(orderNo, user.getId(), path));
    }

    /**
     * 支付宝回调
     * 所有信息都放到request中
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "alipay_callback.do", method = RequestMethod.POST)
    @ResponseBody
    public Object alipayCallBack(HttpServletRequest request) {
        Map<String, String> params = Maps.newHashMap();

        Map requestMap = request.getParameterMap();
        for (Iterator iter = requestMap.keySet().iterator(); iter.hasNext(); ) {
            // 取出key
            String name = (String) iter.next();
            // 取出value
            String[] values = (String[]) requestMap.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
        logger.info("支付宝回调：sign:{},trade_status:{},参数:{}", params.get("sign"), params.get("trade_status"), params.toString());

        // 非常重要，验签，验证回调的正确性，是不是支付宝发的，并且还要避免重复通知
        // 查找jar包里面的class,快捷键是ctrl+shift+t
        // 在支付宝https://docs.open.alipay.com/194/103296/
        // 网页中指出第一步： 在通知返回参数列表中，除去sign、sign_type两个参数外，凡是通知返回回来的参数皆是待验签的参数。
        // 但是验签方法中只除去了sign，我们需要手动移除sign_type
        params.remove("sign_type");

        try {
            boolean alipayRsaCheckV2 = AlipaySignature.rsaCheckV2(params, Configs.getAlipayPublicKey(), "utf-8", Configs.getSignType());
            if (!alipayRsaCheckV2) {
                return ServerResponse.createByErrorMessage("非法请求，验证不通过。再恶意请求我就找网警了。");
            }
        } catch (AlipayApiException e) {
            logger.info("支付宝验证回调异常：", e);
        }
        // 验证各种数据
        ServerResponse serverResponse = iOrderService.alipayCallBack(params);
        if (serverResponse.isSuccess()) {
            return ConstValue.AlipayCallBack.RESPONSE_SUCCESS;
        }
        return ConstValue.AlipayCallBack.RESPONSE_FAILED;
    }

    /**
     * 查询订单支付状态
     *
     * @param session
     * @param orderNo
     * @return
     */
    @RequestMapping(value = "query_order_pay_status.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<Boolean> queryOrderPayStatus(HttpSession session, Long orderNo) {
        User user = (User) session.getAttribute(ConstValue.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        ServerResponse serverResponse = iOrderService.queryOrderPayStatus(user.getId(), orderNo);
        if (serverResponse.isSuccess()) {
            return ServerResponse.createBySuccessData(true);
        }
        return ServerResponse.createBySuccessData(false);
    }

    @RequestMapping(value = "create_order.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse createOrder(HttpSession session, Integer shippingId){
        User user = (User) session.getAttribute(ConstValue.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.createOrder(user.getId(), shippingId);
    }

    @RequestMapping(value = "cancel_order.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse cancelOrder(HttpSession session, Long orderNo){
        User user = (User) session.getAttribute(ConstValue.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.cancelOrder(user.getId(), orderNo);
    }

    @RequestMapping(value = "get_order_cart_product.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getOrderCartProduct(HttpSession session){
        User user = (User) session.getAttribute(ConstValue.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.getOrderCartProduct(user.getId());
    }

    @RequestMapping(value = "order_detail.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<OrderVo> getOrderDetail(HttpSession session, Long orderNo){
        User user = (User) session.getAttribute(ConstValue.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.getOrderDetail(user.getId(), orderNo);
    }

    @RequestMapping(value = "order_list.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getOrderList(HttpSession session,
                                       @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                       @RequestParam(value = "pageSize", defaultValue = "10")int pageSize){
        User user = (User) session.getAttribute(ConstValue.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.getOrderList(user.getId(), pageNum, pageSize);
    }
}
