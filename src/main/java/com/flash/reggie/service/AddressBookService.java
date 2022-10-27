package com.flash.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.flash.reggie.entity.AddressBook;

public interface AddressBookService extends IService<AddressBook> {

    void setDefaultAddress(long userId, long addressId);

}
