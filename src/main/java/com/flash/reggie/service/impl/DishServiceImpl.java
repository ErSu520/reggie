package com.flash.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.flash.reggie.dto.DishDto;
import com.flash.reggie.entity.Category;
import com.flash.reggie.entity.Dish;
import com.flash.reggie.entity.DishFlavor;
import com.flash.reggie.mapper.DishMapper;
import com.flash.reggie.service.CategoryService;
import com.flash.reggie.service.DishFlavorService;
import com.flash.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    @Override
    public void saveWithFlavors(DishDto dishDto) {
        this.save(dishDto);

        List<DishFlavor> flavors = dishDto.getFlavors().stream().map(item -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);
    }

    @Override
    public Page<DishDto> pageWithFlavors(Integer page, Integer pageSize, String name) {
        // 查询菜品信息
        Page<Dish> dishPage = new Page<>(page, pageSize);

        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(name != null, Dish::getName, name);
        lambdaQueryWrapper.orderByDesc(Dish::getUpdateTime);

        this.page(dishPage, lambdaQueryWrapper);

        // 构建返回值
        Page<DishDto> dishDtoPage = new Page<>();
        BeanUtils.copyProperties(dishPage, dishDtoPage, "records");

        dishDtoPage.setRecords(dishPage.getRecords().stream().map(item -> {
            Category category = categoryService.getById(item.getCategoryId());
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            dishDto.setCategoryName(category.getName());
            return dishDto;
        }).collect(Collectors.toList()));


        return dishDtoPage;
    }

    @Override
    public DishDto getByIdWithFlavor(Long dishId) {
        Dish dish = this.getById(dishId);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);

        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.eq(dishId != null, DishFlavor::getDishId, dishId);
        dishDto.setFlavors(dishFlavorService.list(dishFlavorLambdaQueryWrapper));

        dishDto.setCategoryName(categoryService.getById(dish.getCategoryId()).getName());
        return dishDto;
    }

    @Override
    @Transactional
    public void updateWithFlavors(DishDto dishDto) {
        this.updateById(dishDto);

        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
        dishFlavorService.remove(dishFlavorLambdaQueryWrapper);

        List<DishFlavor> list = dishDto.getFlavors().stream().map(item -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(list);
    }

    @Override
    @Transactional
    public void deleteWithFlavors(List<Long> ids) {
        this.removeByIds(ids);


        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.in(DishFlavor::getDishId, ids);
        dishFlavorService.remove(dishFlavorLambdaQueryWrapper);
    }
}
