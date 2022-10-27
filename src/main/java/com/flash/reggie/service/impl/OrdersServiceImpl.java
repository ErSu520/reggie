package com.flash.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.flash.reggie.common.BaseContext;
import com.flash.reggie.common.CustomException;
import com.flash.reggie.common.R;
import com.flash.reggie.entity.*;
import com.flash.reggie.mapper.OrdersMapper;
import com.flash.reggie.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private AddressBookService addressBookService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Override
    @Transactional
    public void createOrder(Orders order) {
        AtomicInteger amount = new AtomicInteger();
        // 保存下单的菜品
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentUserId());
        List<ShoppingCart> shoppingCartList = shoppingCartService.list(shoppingCartLambdaQueryWrapper);
        if(shoppingCartList == null || shoppingCartList.isEmpty()){
            throw new CustomException("购物车为空");
        }
        shoppingCartService.remove(shoppingCartLambdaQueryWrapper);

        // 查询用户信息
        User user = userService.getById(BaseContext.getCurrentUserId());

        // 查询用户订单地址
        AddressBook addressBook = addressBookService.getById(order.getAddressBookId());
        if(addressBook == null){
            throw new CustomException("用户地址有误");
        }

        //生成订单号
        long orderId = IdWorker.getId();

        // 查看购物车内容
        List<OrderDetail> orderDetailList = shoppingCartList.stream().map(shoppingCart -> {
            OrderDetail detail = new OrderDetail();
            BeanUtils.copyProperties(shoppingCart, detail);
            detail.setOrderId(orderId);
            amount.addAndGet(detail.getAmount().multiply(BigDecimal.valueOf(detail.getNumber())).intValue());
            return detail;
        }).collect(Collectors.toList());

        // 订单的信息
        order.setUserId(BaseContext.getCurrentUserId());
        order.setNumber(String.valueOf(orderId));
        order.setOrderTime(LocalDateTime.now());
        order.setCheckoutTime(LocalDateTime.now());
        order.setStatus(2);
        order.setAmount(new BigDecimal(amount.get()));
        order.setUserName(user.getName());
        order.setPhone(addressBook.getPhone());
        order.setConsignee(addressBook.getConsignee());
        order.setAddress(addressBook.getDetail());
        this.save(order);

        orderDetailService.saveBatch(orderDetailList);
    }

}
