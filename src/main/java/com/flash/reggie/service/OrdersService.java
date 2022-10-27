package com.flash.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.flash.reggie.entity.Orders;

public interface OrdersService extends IService<Orders> {

    void createOrder(Orders order);

}
