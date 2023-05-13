package com.example.juan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.juan.entity.ShoppingCart;

public interface ShoppingCartService extends IService<ShoppingCart> {
    /**
     * 添加购物车
     */
    ShoppingCart add(ShoppingCart shoppingCart);

    /**
     * 减少购物车中商品数量
     */
    ShoppingCart sub(ShoppingCart shoppingCart);
}
