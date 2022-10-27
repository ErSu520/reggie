package com.flash.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.flash.reggie.dto.SetmealDto;
import com.flash.reggie.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {

    Page<SetmealDto> pageWithCategory(int page, int pageSize, String name);

    void saveWithDish(SetmealDto setmealDto);

    SetmealDto getWithDish(long id);

    void updateWithDish(SetmealDto setmealDto);

    void deleteWithDish(List<String> ids);

}
