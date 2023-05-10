package com.example.juan.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.juan.common.R;
import com.example.juan.dto.GoodsDto;
import com.example.juan.entity.Category;
import com.example.juan.entity.Goods;
import com.example.juan.service.CategoryService;
import com.example.juan.service.GoodsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@Api(tags = {"商品管理模块"})
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增商品
     */
    @ApiOperation("新增商品")
    @PostMapping
    public R<String> save(@RequestBody Goods goods){
        goodsService.save(goods);

        return R.success("商品新增成功");
    }

    /**
     * 删除商品
     */
    @ApiOperation("删除商品")
    @DeleteMapping
    public R<String> delete(Long id){
        goodsService.removeById(id);

        return R.success("商品删除成功");
    }

    /**
     * 修改商品
     */
    @ApiOperation("修改商品")
    @PutMapping
    public R<String> update(@RequestBody Goods goods){
        goodsService.updateById(goods);

        return R.success("修改成功");
    }

    /**
     * todo:库存修改
     */
//    public R<String> stockUpdate()

    /**
     * 更改商品停起售状态（可批量）
     * @param status
     * @param ids
     * @return
     */
    @ApiOperation("更改商品停起售状态（可批量）")
    @PostMapping("/status/{status}")
    public R<String> changeStatus(@PathVariable int status, long[] ids) {
        //遍历提交的id
        for (long id : ids) {
            //构造update条件
            LambdaUpdateWrapper<Goods> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(Goods::getStatus, status);
            updateWrapper.eq(Goods::getId, id);

            goodsService.update(updateWrapper);
        }

        return R.success("状态更改成功");
    }

    /**
     * 根据 分类Id 或 名称 查询商品
     * @param categoryId 分类id（可选）
     * @param name 商品名称（可选）
     * @return
     */
    @ApiOperation("根据分类Id或名称查询")
    @GetMapping("/list")
    public R<List<Goods>> list(Long categoryId, String name){
        LambdaQueryWrapper<Goods> queryWrapper = new LambdaQueryWrapper<>();
        //只查询状态为1（起售）的商品
        queryWrapper.eq(Goods::getStatus, 1);

        queryWrapper.eq(categoryId!=null, Goods::getCategoryId, categoryId);
        queryWrapper.like(StringUtils.isNotEmpty(name), Goods::getName, name);

        //添加排序条件
        queryWrapper.orderByAsc(Goods::getSort).orderByDesc(Goods::getUpdateTime);

        List<Goods> list = goodsService.list(queryWrapper);
        return R.success(list);
    }

    /**
     * 商品分页查询
     */
    @ApiOperation("商品分页查询")
    @GetMapping("/page")
    public R<Page<GoodsDto>> page(Integer page, Integer pageSize, String name){
        Page<Goods> pageInfo = new Page<>(page, pageSize);

        //若name不为空，添加查询条件
        LambdaQueryWrapper<Goods> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name), Goods::getName, name);
        //添加排序条件
        queryWrapper.orderByDesc(Goods::getUpdateTime);

        //将查出来的数据封装到pageInfo，但此时Goods对象中没有分类名称
        goodsService.page(pageInfo, queryWrapper);

        //借助GoodsDto携带分类名称
        Page<GoodsDto> goodsDtoPage = new Page<>();
        //将records以外的属性拷贝到goodsDtoPage中
        BeanUtils.copyProperties(pageInfo,goodsDtoPage,"records");

        //构建List<GoodsDto>,作为 goodsDtoPage 的 records
        List<Goods> goodsList = pageInfo.getRecords();
        List<GoodsDto> goodsDtoList = new ArrayList<>();

        for (Goods goods : goodsList){
            GoodsDto goodsDto = new GoodsDto();
            BeanUtils.copyProperties(goods, goodsDto);

            //用 categoryId 将 categoryName 查出来
            Category category = categoryService.getById(goods.getCategoryId());
            //为dishDto的categoryName属性赋值
            goodsDto.setCategoryName(category.getName());

            goodsDtoList.add(goodsDto);
        }

        goodsDtoPage.setRecords(goodsDtoList);
        return R.success(goodsDtoPage);
    }
}
