package com.example.juan.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.juan.common.R;
import com.example.juan.entity.ShoppingCart;
import com.example.juan.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@RestController
@Api(tags = {"购物车管理模块"})
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 添加购物车
     */
    @ApiOperation("添加购物车")
    @PostMapping("/add")
    public R<ShoppingCart> add(HttpSession session, @RequestBody ShoppingCart shoppingCart){
        //获取当前用户Id
        Long userId = (Long) session.getAttribute("user");
        //设置用户Id
        shoppingCart.setUserId(userId);
        //shoppingCart需要携带userId
        ShoppingCart one = shoppingCartService.add(shoppingCart);

        return R.success(one);
    }

    /**
     * 减少购物车中商品的数量
     * 需要前端传来该条购物车记录的id
     */
    @ApiOperation("减少购物车中商品的数量")
    @PostMapping("/sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart){
        shoppingCart = shoppingCartService.sub(shoppingCart);

        if (shoppingCart == null){
            return R.error("数量错误");
        }else {
            return R.success(shoppingCart);
        }
    }


    /**
     * 显示当前购物车
     */
    @ApiOperation("显示当前购物车")
    @GetMapping("/list")
    public R<List<ShoppingCart>> list(HttpSession session){
        Long userId = (Long) session.getAttribute("user");

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, userId);

        List<ShoppingCart> shoppingCartList = shoppingCartService.list(queryWrapper);

        return R.success(shoppingCartList);
    }

    /**
     * 清空购物车
     */
    @ApiOperation("清空购物车")
    @DeleteMapping("/clean")
    public R<String> clean(HttpSession session){
        Long userId = (Long) session.getAttribute("user");

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, userId);
        shoppingCartService.remove(queryWrapper);

        return R.success("清空购物车成功");
    }
}
