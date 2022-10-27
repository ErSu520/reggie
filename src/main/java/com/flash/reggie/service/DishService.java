package com.flash.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.flash.reggie.dto.DishDto;
import com.flash.reggie.entity.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {

    void saveWithFlavors(DishDto dishDto);

    Page<DishDto> pageWithFlavors(Integer page, Integer pageSize, String name);

    DishDto getByIdWithFlavor(Long dishId);

    void updateWithFlavors(DishDto dishDto);

    void deleteWithFlavors(List<Long> ids);

}
