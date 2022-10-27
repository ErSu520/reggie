package com.flash.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.flash.reggie.entity.AddressBook;
import com.flash.reggie.mapper.AddressBookMapper;
import com.flash.reggie.service.AddressBookService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {

    @Override
    @Transactional
    public void setDefaultAddress(long userId, long addressId) {
        // 将原有的默认地址改为非默认
        LambdaUpdateWrapper<AddressBook> addressBookLambdaQueryWrapper = new LambdaUpdateWrapper<>();
        addressBookLambdaQueryWrapper.eq(AddressBook::getUserId, userId);
        addressBookLambdaQueryWrapper.set(AddressBook::getIsDefault, 0);
        this.update(addressBookLambdaQueryWrapper);

        // 将指定的地址改为默认
        addressBookLambdaQueryWrapper.clear();
        addressBookLambdaQueryWrapper.eq(AddressBook::getId, addressId);
        addressBookLambdaQueryWrapper.set(AddressBook::getIsDefault, 1);
        this.update(addressBookLambdaQueryWrapper);
    }

}
