package com.example.juan.dto;

import com.example.juan.entity.Goods;
import lombok.Data;

@Data
public class GoodsDto extends Goods {
    //分类名称
    private String categoryName;
}
