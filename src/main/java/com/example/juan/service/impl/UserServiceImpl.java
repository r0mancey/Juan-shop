package com.example.juan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.juan.entity.User;
import com.example.juan.mapper.UserMapper;
import com.example.juan.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    /**
     * 验证密码
     * @param id 用户id
     * @param password 需要验证的密码
     * @return 验证成功则返回true
     */
    @Override
    public boolean checkPassword(Long id, String password) {
        //将需要验证的密码进行MD5加密
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //进行比对
        return getById(id).getPassword().equals(password);
    }
}
