package com.example.juan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.juan.entity.User;


public interface UserService extends IService<User> {
    /**
     * 验证密码
     */
    boolean checkPassword(Long id, String password);
}
