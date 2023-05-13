package com.example.juan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.juan.common.R;
import com.example.juan.entity.Goods;
import com.example.juan.entity.ShoppingCart;
import com.example.juan.mapper.ShoppingCartMapper;
import com.example.juan.service.GoodsService;
import com.example.juan.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

    @Autowired
    private GoodsService goodsService;

    /**
     * 添加购物车
     * @param shoppingCart 需要携带userId
     */
    @Override
    public ShoppingCart add(ShoppingCart shoppingCart) {
        //判断此条数据是否存在
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, shoppingCart.getUserId());
        queryWrapper.eq(ShoppingCart::getGoodsId, shoppingCart.getGoodsId());
        ShoppingCart one = this.getOne(queryWrapper);


        if (one!=null){
            //存在则数量+1
            Integer number = one.getNumber();
            one.setNumber(++number);
            //金额+一份单价
            BigDecimal amount = one.getAmount();
            one.setAmount(amount.add(one.getPrice())); //单价在属性中有记录
            this.updateById(one);
        }else {
            //不存在则插入
            one = shoppingCart;
            one.setNumber(1);   //即使数据库有默认值，但因为要返回前端，所以仍要设值

            //由于记录不存在，所以要查询商品表获取商品单价
            Goods goods = goodsService.getById(shoppingCart.getGoodsId());
            BigDecimal price = goods.getPrice();
            one.setAmount(price);
            one.setPrice(price);

            //为商品名称和图片赋值
            one.setName(goods.getName());
            one.setImage(goods.getImage());

            this.save(one);
        }

        return one;
    }

    /**
     * 减少购物车中商品数量
     * @param shoppingCart 需要携带该条记录的id
     */
    @Override
    public ShoppingCart sub(ShoppingCart shoppingCart) {
        //当前数量减1
        shoppingCart = this.getById(shoppingCart.getId());
        Integer number = shoppingCart.getNumber();
        shoppingCart.setNumber(--number);

        //如果数量大于0，则更新
        if (number > 0){
            //金额减一份单价
            BigDecimal amount = shoppingCart.getAmount();
            amount = amount.subtract(shoppingCart.getPrice());
            shoppingCart.setAmount(amount);

            this.updateById(shoppingCart);
        }
        //如果等于0，则删除该条数据
        else if (number == 0){
            this.removeById(shoppingCart.getId());
        }
        //返回null表示数量错误
        else {
            return null;
        }

        return shoppingCart;
    }
}
