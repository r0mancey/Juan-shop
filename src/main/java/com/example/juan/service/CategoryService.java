package com.example.juan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.juan.entity.Category;

public interface CategoryService extends IService<Category> {
    /**
     * 只能删除没有关联商品的分类
     */
    public void remove(Long categoryId);
}
