package com.example.juan.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.juan.common.R;
import com.example.juan.entity.AddressBook;
import com.example.juan.service.AddressBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@RestController
@Api(tags = {"地址管理模块"})
@RequestMapping("/addressBook")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    /**
     * 新增地址
     */
    @ApiOperation("新增地址")
    @PostMapping
    public R<String> save(HttpSession session, @RequestBody AddressBook addressBook){
        //从session中取得当前userId
        Long userId = (Long) session.getAttribute("user");
        log.info("当前用户Id：{}",userId.toString());

        addressBook.setUserId(userId);
        addressBookService.save(addressBook);

        return R.success("新增地址成功");
    }

    /**
     * 展示当前用户的所有地址
     */
    @ApiOperation("展示当前用户的所有地址")
    @GetMapping("/list")
    public R<List<AddressBook>> list(HttpSession session){
        //从session中取得当前userId
        Long userId = (Long) session.getAttribute("user");

        //获取Id下所有地址
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, userId);
        List<AddressBook> addressBookList = addressBookService.list(queryWrapper);

        return R.success(addressBookList);
    }

    /**
     * 设为默认地址
     * @param addressBook 前端用json传来了addressBook的id
     */
    @ApiOperation("设为默认地址")
    @PutMapping("/default")
    public R<String> setDefault(@RequestBody AddressBook addressBook){
        addressBookService.setDefault(addressBook);

        return R.success("设置成功");
    }

    /**
     * 根据Id回显地址信息
     */
    @ApiOperation("根据Id回显地址信息")
    @GetMapping("/{id}")
    public R<AddressBook> getById(@PathVariable Long id){
        AddressBook addressBook = addressBookService.getById(id);

        return R.success(addressBook);
    }

    /**
     * 修改地址信息
     */
    @ApiOperation("修改地址信息")
    @PutMapping
    public R<String> update(@RequestBody AddressBook addressBook){
        addressBookService.updateById(addressBook);

        return R.success("修改成功");
    }

    /**
     * 删除地址信息
     */
    @ApiOperation("删除地址信息")
    @DeleteMapping
    public R<String> delete(Long id){
        addressBookService.removeById(id);

        return R.success("删除成功");
    }

    /**
     * 获取默认地址
     */
    @ApiOperation("获取默认地址")
    @GetMapping("/default")
    public R<AddressBook> getDefault(HttpSession session){
        Long userId = (Long) session.getAttribute("user");

        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, userId);
        queryWrapper.eq(AddressBook::getIsDefault, 1);
        AddressBook addressBook = addressBookService.getOne(queryWrapper);

        return R.success(addressBook);
    }
}
