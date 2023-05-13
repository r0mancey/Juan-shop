package com.example.juan.entity;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 购物车
 */
@Data
public class ShoppingCart implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    //用户id
    private Long userId;

    //商品id
    private Long goodsId;

    //数量
    private Integer number;

    //金额
    private BigDecimal amount;

    //商品单价（冗余字段）
    private BigDecimal price;

    //名称（冗余字段）
    private String name;

    //图片（冗余字段）
    private String image;

}

