package com.example.juan.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.juan.common.R;
import com.example.juan.entity.User;
import com.example.juan.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@Api(tags = {"用户管理模块"})
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册
     */
    @ApiOperation("用户注册")
    @PostMapping("/register")
    public R<String> register(@RequestBody User user){
        //校验手机号是否曾注册
        String phone = user.getPhone();
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone, phone);
        int count = userService.count(queryWrapper);
        if (count > 0) return R.error("该手机号已被注册！");

        //将密码进行md5加密
        String password = user.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        user.setPassword(password);

        userService.save(user);
        return R.success("注册成功");
    }

    /**
     * 用户登录
     */
    @ApiOperation("用户登录")
    @PostMapping("/login")
    public R<User> login(HttpServletRequest request, @RequestBody User user){
        //先比对用户名
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone, user.getPhone());
        User one = userService.getOne(queryWrapper);

        if (one == null){
            return R.error("登录失败");
        }

        //再比对密码
        String password = user.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        if (!one.getPassword().equals(password)){
            return R.error("登录失败");
        }

        //检查账号是否被禁用
        if (one.getStatus() == 0){
            return R.error("该账号已被禁用");
        }

        //登录成功，将用户id存入session
        request.getSession().setAttribute("user",one.getId());
        return R.success(one);
    }

    /**
     * 用户登出
     */
    @ApiOperation("用户登出")
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        //清理Session中保存的id
        request.getSession().removeAttribute("user");
        return R.success("登出成功");
    }

    /**
     * 用户分页查询
     */
    @ApiOperation("用户分页查询")
    @GetMapping("/page")
    public R<Page<User>> page(Integer page, Integer pageSize, String phone){
        Page<User> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(phone), User::getPhone, phone);
        queryWrapper.orderByDesc(User::getUpdateTime);
        userService.page(pageInfo, queryWrapper);

        return R.success(pageInfo);
    }

    /**
     * 密码验证
     */
    @ApiOperation("密码验证")
    @GetMapping("/checkPassword")
    public R<String> checkPassword(Long id, String password){
        if (userService.checkPassword(id, password)){
            return R.success("密码正确");
        }else {
            return R.error("密码错误");
        }
    }

    /**
     * 删除用户
     */
    @ApiOperation("删除用户")
    @DeleteMapping
    public R<String> delete(Long id){
        userService.removeById(id);
        return R.success("用户删除成功");
    }

    /**
     * 修改用户信息
     */
    @ApiOperation("修改用户信息")
    @PutMapping
    public R<String> update(@RequestBody User user){
        //todo：修改信息时不能修改密码
       userService.updateById(user);
       return R.success("修改成功");
    }

    //todo：修改密码或更改用户状态时，公共字段填充不生效

    /**
     * 修改密码
     * @param id 用户id
     * @param oldPassword 原密码
     * @param newPassword 新密码
     */
    @ApiOperation("修改用户密码")
    @PutMapping("/password")
    public R<String> updatePassword(Long id, String oldPassword, String newPassword){
        //验证原密码
        if (!userService.checkPassword(id, oldPassword)){
            return R.error("请输入正确的原密码！");
        }

        //设置新密码
        newPassword = DigestUtils.md5DigestAsHex(newPassword.getBytes());
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(User::getId, id);
        updateWrapper.set(User::getPassword, newPassword);
        userService.update(updateWrapper);

        return R.success("密码修改成功");
    }

    //todo：忘记密码发送邮件，及重置密码

    /**
     * 禁用或恢复用户
     */
    @ApiOperation("禁用或恢复用户")
    @PutMapping("/status/{status}")
    public R<String> changeStatus(@PathVariable Integer status, Long id) {
        //将用户状态更改为status
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(User::getStatus, status);
        updateWrapper.eq(User::getId, id);
        userService.update(updateWrapper);

        return R.success("状态更改成功");
    }
}
