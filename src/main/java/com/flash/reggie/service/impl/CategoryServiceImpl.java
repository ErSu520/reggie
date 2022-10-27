package com.flash.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.flash.reggie.common.CustomException;
import com.flash.reggie.common.R;
import com.flash.reggie.entity.Category;
import com.flash.reggie.entity.Dish;
import com.flash.reggie.entity.Setmeal;
import com.flash.reggie.mapper.CategoryMapper;
import com.flash.reggie.service.CategoryService;
import com.flash.reggie.service.DishService;
import com.flash.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    DishService dishService;

    @Autowired
    SetmealService setmealService;

    @Override
    public void remove(Long id){
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        int dishCount = dishService.count(dishLambdaQueryWrapper);
        if(dishCount > 0){
            throw new CustomException("当前分类下关联了菜品，不能删除");
        }

        LambdaQueryWrapper<Setmeal> setmealServiceLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealServiceLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        int setmealCout = setmealService.count(setmealServiceLambdaQueryWrapper);
        if(setmealCout > 0){
            throw new CustomException("当前分类下关联了套餐，不能删除");
        }

        this.removeById(id);
    }

}
