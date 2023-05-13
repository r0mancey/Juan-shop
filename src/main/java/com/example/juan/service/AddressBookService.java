package com.example.juan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.juan.entity.AddressBook;

public interface AddressBookService extends IService<AddressBook> {
    void setDefault(AddressBook addressBook);
}
