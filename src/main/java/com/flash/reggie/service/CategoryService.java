package com.flash.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.flash.reggie.entity.Category;

public interface CategoryService extends IService<Category> {

    void remove(Long id);

}
