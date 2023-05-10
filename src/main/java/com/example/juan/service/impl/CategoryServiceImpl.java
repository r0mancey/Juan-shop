package com.example.juan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.juan.common.CustomException;
import com.example.juan.entity.Category;
import com.example.juan.entity.Goods;
import com.example.juan.mapper.CategoryMapper;
import com.example.juan.service.CategoryService;
import com.example.juan.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private GoodsService goodsService;

    /**
     * 只能删除没有关联商品的分类
     */
    @Override
    public void remove(Long categoryId) {
        //查询商品表是否有关联了该分类的商品
        LambdaQueryWrapper<Goods> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Goods::getCategoryId, categoryId);
        int count = goodsService.count(queryWrapper);

        //若有，抛出异常
        if (count > 0) throw new CustomException("该分类关联了商品，不能删除！");

        //否则，正常删除
        super.removeById(categoryId);
    }
}
