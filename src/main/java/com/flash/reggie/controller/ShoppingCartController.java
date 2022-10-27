package com.flash.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.flash.reggie.common.BaseContext;
import com.flash.reggie.common.R;
import com.flash.reggie.entity.ShoppingCart;
import com.flash.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @RequestMapping("/list")
    public R<List<ShoppingCart>> list(){
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentUserId())
                .orderByDesc(ShoppingCart::getCreateTime);

        return R.success(shoppingCartService.list(shoppingCartLambdaQueryWrapper));
    }

    @PostMapping("/add")
    public R<String> save(@RequestBody ShoppingCart shoppingCart){
        shoppingCart.setUserId(BaseContext.getCurrentUserId());

        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId, shoppingCart.getUserId())
                        .eq(shoppingCart.getSetmealId() != null, ShoppingCart::getSetmealId, shoppingCart.getSetmealId())
                        .eq(shoppingCart.getDishId() != null, ShoppingCart::getDishId, shoppingCart.getDishId());


        ShoppingCart shoppingCartInfo = shoppingCartService.getOne(shoppingCartLambdaQueryWrapper);
        if(shoppingCartInfo == null) {
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
        }else {
            shoppingCartInfo.setNumber(shoppingCartInfo.getNumber() + 1);
            shoppingCartService.updateById(shoppingCartInfo);
        }
        return R.success("菜品已加入购物车");
    }

    @PostMapping("/sub")
    public R<String> delete(@RequestBody ShoppingCart shoppingCart){
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentUserId());
        shoppingCartLambdaQueryWrapper.eq(shoppingCart.getDishId() != null, ShoppingCart::getDishId, shoppingCart.getDishId());
        shoppingCartLambdaQueryWrapper.eq(shoppingCart.getSetmealId() != null, ShoppingCart::getSetmealId, shoppingCart.getSetmealId());

        ShoppingCart shoppingCartInfo = shoppingCartService.getOne(shoppingCartLambdaQueryWrapper, false);
        if(shoppingCartInfo == null){
            return  R.error("购物车中没有此菜品");
        }
        if(shoppingCartInfo.getNumber() < 2){
            shoppingCartService.remove(shoppingCartLambdaQueryWrapper);
        }else {
            shoppingCartInfo.setNumber(shoppingCartInfo.getNumber() - 1);
            shoppingCartService.updateById(shoppingCartInfo);
        }
        return R.success("减少数量成功");
    }

    @DeleteMapping("/clean")
    public R<String> clean(){
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentUserId());

        shoppingCartService.remove(shoppingCartLambdaQueryWrapper);
        return R.success("已清空购物车");
    }

}
