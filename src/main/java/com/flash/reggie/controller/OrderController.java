package com.flash.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.flash.reggie.common.BaseContext;
import com.flash.reggie.common.R;
import com.flash.reggie.entity.Orders;
import com.flash.reggie.entity.ShoppingCart;
import com.flash.reggie.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrdersService ordersService;

    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        ordersService.createOrder(orders);
        return R.success("订单提交成功");
    }

    @GetMapping("/userPage")
    public R<Page> userPage(int page, int pageSize){
        LambdaQueryWrapper<Orders> ordersLambdaQueryWrapper = new LambdaQueryWrapper<>();
        ordersLambdaQueryWrapper.eq(Orders::getUserId, BaseContext.getCurrentUserId())
                .orderByDesc(Orders::getOrderTime);

        Page<Orders> pageInfo = new Page<>(page, pageSize);
        ordersService.page(pageInfo, ordersLambdaQueryWrapper);
        return R.success(pageInfo);
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, Long number, LocalDateTime beginTime, LocalDateTime endTime){
        LambdaQueryWrapper<Orders> ordersLambdaQueryWrapper = new LambdaQueryWrapper<>();
        ordersLambdaQueryWrapper.eq(number != null, Orders::getId, number)
                .between(beginTime != null && endTime != null, Orders::getOrderTime, beginTime, endTime)
                .orderByDesc(Orders::getOrderTime);

        Page<Orders> pageInfo = new Page<>(page, pageSize);
        ordersService.page(pageInfo, ordersLambdaQueryWrapper);
        return R.success(pageInfo);
    }

    @PutMapping
    public R<String> update(@RequestBody Orders orders){
        ordersService.updateById(orders);
        return R.success("修改成功");
    }

}
