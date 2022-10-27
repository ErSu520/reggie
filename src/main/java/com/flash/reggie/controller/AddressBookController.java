package com.flash.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.flash.reggie.common.BaseContext;
import com.flash.reggie.common.R;
import com.flash.reggie.entity.AddressBook;
import com.flash.reggie.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/addressBook")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    @GetMapping("/list")
    public R<List> list(){
        LambdaQueryWrapper<AddressBook> addressBookLambdaQueryWrapper = new LambdaQueryWrapper<>();
        addressBookLambdaQueryWrapper.eq(AddressBook::getUserId, BaseContext.getCurrentUserId());
        addressBookLambdaQueryWrapper.orderByDesc(AddressBook::getUpdateTime);

        List<AddressBook> list = addressBookService.list(addressBookLambdaQueryWrapper);
        return R.success(list);
    }

    @PostMapping
    public R<String> save(@RequestBody AddressBook addressBook){
        addressBook.setUserId(BaseContext.getCurrentUserId());
        addressBookService.save(addressBook);

        return R.success("添加成功");
    }

    @PutMapping("/default")
    public R<String> setDefaultAddress(@RequestBody AddressBook addressBook){
        addressBookService.setDefaultAddress(BaseContext.getCurrentUserId(), addressBook.getId());
        return R.success("设置默认地址成功");
    }

    @GetMapping("/default")
    public R<AddressBook> getDefaultAddress(){
        LambdaQueryWrapper<AddressBook> addressBookLambdaQueryWrapper = new LambdaQueryWrapper<>();
        addressBookLambdaQueryWrapper.eq(AddressBook::getUserId, BaseContext.getCurrentUserId());
        addressBookLambdaQueryWrapper.eq(AddressBook::getIsDefault, 1);

        AddressBook addressBook = addressBookService.getOne(addressBookLambdaQueryWrapper);

        return addressBook == null ? R.error("用户没有设置默认地址") : R.success(addressBook);
    }

}
