package com.example.juan.service.impl;


import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.juan.entity.AddressBook;
import com.example.juan.mapper.AddressBookMapper;
import com.example.juan.service.AddressBookService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
    /**
     * 设为默认地址
     * @param addressBook 只携带了addressId
     */
    @Override
    @Transactional  //涉及多次表操作
    public void setDefault(AddressBook addressBook) {
        //通过addressId查出完整记录
        addressBook = this.getById(addressBook.getId());
        //得到对应的用户Id
        Long userId = addressBook.getUserId();
        //将该用户所有地址默认状态设为0
        LambdaUpdateWrapper<AddressBook> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(AddressBook::getUserId, userId);
        updateWrapper.set(AddressBook::getIsDefault, 0);
        this.update(updateWrapper);
        //将当前地址设为默认
        addressBook.setIsDefault(1);
        this.updateById(addressBook);
    }
}
