package com.example.juan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.juan.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
