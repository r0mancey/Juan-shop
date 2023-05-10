package com.example.juan.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.juan.common.R;
import com.example.juan.entity.Category;
import com.example.juan.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@Api(tags = {"商品分类管理模块"})
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增分类
     * @param category
     * @return
     */
    @ApiOperation("新增分类")
    @PostMapping
    public R<String> save(@RequestBody Category category){
        categoryService.save(category);
        return R.success("保存成功");
    }

    /**
     * 分类信息分页查询
     * @param page
     * @param pageSize
     * @return
     */
    @ApiOperation("分类信息分页查询")
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize){
        //构造分页构造器
        Page<Category> pageInfo = new Page(page, pageSize);

        //构造条件过滤器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();

        //添加排序条件
        queryWrapper.orderByAsc(Category::getSort);

        categoryService.page(pageInfo, queryWrapper);

        return R.success(pageInfo);
    }

    /**
     * 删除分类
     * @param id
     * @return
     */
    @ApiOperation("删除分类")
    @DeleteMapping
    public R<String> delete(Long id){
        //分类与商品之间关联则不能删除
        categoryService.remove(id);

        return R.success("删除成功");
    }

    /**
     * 修改分类信息
     * @param category
     * @return
     */
    @ApiOperation("修改分类信息")
    @PutMapping
    public R<String> update(@RequestBody Category category){
        categoryService.updateById(category);
        return R.success("修改分类信息成功");
    }

    /**
     * 查找所有分类
     * （用于在用户端显示所有分类）
     */
    @ApiOperation("查找所有分类")
    @GetMapping("/list")
    public R<List<Category>> list(){
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);
    }

}
