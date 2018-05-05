package com.mmall.service;

import com.mmall.common.ServerResponse;

/**
 * @program: mmall
 * @description:
 * @author: Ypwang1024
 * @create: 2018-05-05 22:20
 **/
public interface ICategoryService {

    ServerResponse addCategory(String categoryName, Integer parentId);

    ServerResponse updateCategoryName(Integer categoryId, String categoryName);
}
