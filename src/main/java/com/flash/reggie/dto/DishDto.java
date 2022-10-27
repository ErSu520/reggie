package com.flash.reggie.dto;

import com.flash.reggie.entity.Dish;
import com.flash.reggie.entity.DishFlavor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;

    @Override
    public String toString() {
        return categoryName + "  " + copies + "  " + flavors.size();
    }
}
