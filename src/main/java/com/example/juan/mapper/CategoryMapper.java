package com.example.juan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.juan.entity.Category;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
