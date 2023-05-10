package com.example.juan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.juan.entity.Goods;
import com.example.juan.mapper.GoodsMapper;
import com.example.juan.service.GoodsService;
import org.springframework.stereotype.Service;

@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {
}
