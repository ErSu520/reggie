package com.flash.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.flash.reggie.common.R;
import com.flash.reggie.dto.DishDto;
import com.flash.reggie.entity.Category;
import com.flash.reggie.entity.Dish;
import com.flash.reggie.entity.DishFlavor;
import com.flash.reggie.service.CategoryService;
import com.flash.reggie.service.DishFlavorService;
import com.flash.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());
        dishService.saveWithFlavors(dishDto);
        return R.success("保存成功");
    }

    @GetMapping("/page")
    public R<Page> page(Integer page, Integer pageSize, String name){
        log.info(name + "   " + page + "   " + pageSize);
        //初始化默认值
        page = page == null ? 1 : page;
        pageSize = pageSize == null ? 10 : pageSize;

        Page<DishDto> pageInfo = dishService.pageWithFlavors(page, pageSize, name);
        return R.success(pageInfo);
    }

    @GetMapping("/{id}")
    public R<DishDto> single(@PathVariable Long id){
        DishDto dishDto = null;
        if(id != null) {
            dishDto = dishService.getByIdWithFlavor(id);
        }
        return R.success(dishDto);
    }

    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        dishService.updateWithFlavors(dishDto);
        return R.success("更新成功");
    }

    @PostMapping("status/{status}")
    public R<String> startOrStopSell(@PathVariable int status, String ids){
        for(String id : ids.split(",")){
            Dish dish = dishService.getById(id);
            dish.setStatus(status);
            dishService.updateById(dish);
        }
        return R.success("修改成功");
    }

    @DeleteMapping
    public R<String> delete(String ids){
        if (ids != null) {
            List<Long> idList =Arrays.stream(ids.split(",")).map(id -> Long.valueOf(id)).collect(Collectors.toList());
            dishService.deleteWithFlavors(idList);
        }
        return R.success("删除成功");
    }

//    @GetMapping("list")
//    public R<List> list(Dish dish){
//        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
//        dishLambdaQueryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId())
//                .orderByAsc(Dish::getSort)
//                .orderByDesc(Dish::getUpdateTime);
//
//        List<Dish> list = dishService.list(dishLambdaQueryWrapper);
//        return R.success(list);
//    }

    @GetMapping("list")
    public R<List> list(Dish dish){
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId())
                .orderByAsc(Dish::getSort)
                .orderByDesc(Dish::getUpdateTime);

        List<Dish> list = dishService.list(dishLambdaQueryWrapper);

        List<DishDto> dishDtoList = list.stream().map(item ->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);

            LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
            dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId, item.getId());
            dishDto.setFlavors(dishFlavorService.list(dishFlavorLambdaQueryWrapper));

            return dishDto;
        }).collect(Collectors.toList());

        return R.success(dishDtoList);
    }

}
