package com.example.juan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.juan.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {
}
