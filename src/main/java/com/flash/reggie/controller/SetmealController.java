package com.flash.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.flash.reggie.common.R;
import com.flash.reggie.dto.SetmealDto;
import com.flash.reggie.entity.Category;
import com.flash.reggie.entity.Setmeal;
import com.flash.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        Page<SetmealDto> setmealPage = setmealService.pageWithCategory(page, pageSize, name);
        return R.success(setmealPage);
    }

    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        setmealService.saveWithDish(setmealDto);
        return R.success("添加成功");
    }

    @GetMapping("/{id}")
    public R<SetmealDto> single(@PathVariable long id){
        SetmealDto setmealDto = setmealService.getWithDish(id);
        return R.success(setmealDto);
    }

    @PutMapping
    public R<String> update(@RequestBody SetmealDto setmealDto){
        setmealService.updateWithDish(setmealDto);
        return R.success("更新成功");
    }

    @PostMapping("/status/{status}")
    public R<String> startOrStopSell(@PathVariable int status, String ids){
        if(ids == null){
            return R.error("修改失败");
        }
        List<String> list =  Arrays.stream(ids.split(",")).collect(Collectors.toList());
        List<Setmeal> setmealList = setmealService.listByIds(list);
        setmealList.stream().map(item -> {
            item.setStatus(status);
            return item;
        }).collect(Collectors.toList());
        setmealService.updateBatchById(setmealList);
        return R.success("修改成功");
    }

    @DeleteMapping
    public R<String> delete(@RequestParam List<String> ids){
        setmealService.deleteWithDish(ids);
        return R.success("删除成功");
    }

    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, setmeal.getCategoryId())
                .eq(Setmeal::getStatus, setmeal.getStatus())
                .orderByAsc(Setmeal::getCode)
                .orderByDesc(Setmeal::getUpdateTime);

        return R.success(setmealService.list(setmealLambdaQueryWrapper));
    }


}
