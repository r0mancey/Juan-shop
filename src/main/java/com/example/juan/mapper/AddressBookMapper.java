package com.example.juan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.juan.entity.AddressBook;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {
}
