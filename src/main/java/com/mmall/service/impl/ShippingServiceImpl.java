package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.mmall.common.ServerResponse;
import com.mmall.dao.ShippingMapper;
import com.mmall.pojo.Shipping;
import com.mmall.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @program: mmall
 * @description:收货地址接口实现类
 * @author: ypwang
 * @create: 2018-05-30 07:59
 **/
@Service("iShippingService")
public class ShippingServiceImpl implements IShippingService {

    @Autowired
    private ShippingMapper shippingMapper;

    @Override
    public ServerResponse<Map> addShipping(Integer userId, Shipping shipping) {
        shipping.setUserId(userId);
        // 在这里需要在完成插入操作后拿到新建的主键ID,那么这<select 中添加配置>后：将ID放到shipping中
        // <insert id="insert" parameterType="com.mmall.pojo.Shipping" useGeneratedKeys="true" keyProperty="id">
        int rowCount = shippingMapper.insert(shipping);
        if (rowCount > 0) {
            Map result = Maps.newHashMap();
            result.put("shippingId", shipping.getId());
            return ServerResponse.createBySuccessMessageData("新建地址成功", result);
        }
        return ServerResponse.createByErrorMessage("新建地址失败");
    }

    @Override
    public ServerResponse<String> deleteShipping(Integer userId, Integer shippingId) {
        // int resultCount = shippingMapper.deleteByPrimaryKey(shippingId);
        // 写到这里删除方法就完成了吗？存在横向越权这么一个场景，一个用户已经登录，那么只要传一个shippingId过来，
        // shippingId可能是自己的，也可能是他人的，这样就可能把他人地址也删除。
        // 这里需要把shippingId和userId关联起来，不能使用shippingMapper.deleteByPrimaryKey(shippingId);
        int resultCount = shippingMapper.deleteByUserIdAndShippingId(userId, shippingId);
        if (resultCount > 0) {
            return ServerResponse.createByErrorMessage("删除地址成功");
        }
        return ServerResponse.createByErrorMessage("删除地址失败");
    }

    @Override
    public ServerResponse updateShipping(Integer userId, Shipping shipping) {
        shipping.setUserId(userId);
        int rowCount = shippingMapper.updateByShipping(shipping);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("更新地址成功");
        }
        return ServerResponse.createByErrorMessage("更新地址失败");
    }

    @Override
    public ServerResponse<Shipping> selectShipping(Integer userId, Integer shippingId) {
        Shipping shipping = shippingMapper.selectShippingByIdAndUserId(userId, shippingId);
        if (shipping == null) {
            return ServerResponse.createByErrorMessage("无法查询到该地址");
        }
        return ServerResponse.createBySuccessMessageData("更新地址成功", shipping);
    }

    @Override
    public ServerResponse<PageInfo> shippingList(Integer userId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Shipping> shippingList = shippingMapper.selectShippingListByUserId(userId);
        PageInfo pageInfo = new PageInfo(shippingList);
        return ServerResponse.createBySuccessData(pageInfo);
    }
}
