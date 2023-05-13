package com.example.juan.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.juan.common.R;
import com.example.juan.entity.Admin;
import com.example.juan.service.AdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Api(tags = {"管理员模块"})
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    /**
     * 管理员登录
     */
    @ApiOperation("管理员登录")
    @PostMapping("/login")
    public R<Admin> login(HttpServletRequest request, @RequestBody Admin admin){
        //1、将页面提交的密码进行md5加密
        String password = admin.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //2、根据页面提交用户名username查找数据库
        LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Admin::getUsername,admin.getUsername());
        Admin one = adminService.getOne(queryWrapper);

        //3、对比用户名
        if (one == null){
            return R.error("登录失败");
        }

        //4、对比密码
        if (!one.getPassword().equals(password)){
            return R.error("登录失败");
        }

        //5、登录成功，将管理员id存入session
        request.getSession().setAttribute("admin",one.getId());
        return R.success(one);

    }

    /**
     * 管理员登出
     */
    @ApiOperation("管理员登出")
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        //清理Session中保存的管理员id
        request.getSession().removeAttribute("admin");
        return R.success("登出成功");
    }


    /**
     * 修改管理员信息（除密码外）
     */
    @ApiOperation("修改管理员信息（除密码外）")
    @PutMapping
    public R<String> update(@RequestBody Admin admin){
        //执行更新操作
        adminService.updateById(admin);

        return R.success("修改成功");
    }

    /**
     * 修改密码
     */
    @ApiOperation("修改密码")
    @PutMapping("/updatePassword")
    public R<String> updatePassword(@RequestBody Admin admin){
        String password = admin.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        admin.setPassword(password);
        adminService.updateById(admin);

        return R.success("修改成功");
    }

    /**
     * 根据ID查管理员信息
     */
    @ApiOperation("根据ID查管理员信息")
    @GetMapping("/{id}")
    public R<Admin> getById(@PathVariable Long id){
        Admin admin = adminService.getById(id);
        return R.success(admin);
    }
}
