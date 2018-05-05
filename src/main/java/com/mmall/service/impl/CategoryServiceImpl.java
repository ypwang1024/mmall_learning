package com.mmall.service.impl;

import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: mmall
 * @description:
 * @author: ypwang
 * @create: 2018-05-05 22:20
 **/
@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 添加品类
     *
     * @param categoryName
     * @param parentId
     * @return
     */
    @Override
    public ServerResponse addCategory(String categoryName, Integer parentId) {
        // 校验参数
        if (parentId == null || StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByErrorMessage("添加品类参数错误");
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        /**这个分类是可用的*/
        category.setStatus(true);

        // 执行数据插入
        int rowCount = categoryMapper.insert(category);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("添加品类成功");
        }
        return ServerResponse.createByErrorMessage("添加品类失败");
    }

    /**
     * 执行品类更新名称
     *
     * @param categoryId
     * @param categoryName
     * @return
     */
    @Override
    public ServerResponse updateCategoryName(Integer categoryId, String categoryName) {
        // 校验参数
        if (categoryId == null || StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByErrorMessage("更新品类参数错误");
        }
        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);

        int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("更新品类成功");
        }
        return ServerResponse.createByErrorMessage("更新品类失败");
    }
}
