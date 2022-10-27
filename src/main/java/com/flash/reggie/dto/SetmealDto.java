package com.flash.reggie.dto;


import com.flash.reggie.entity.Setmeal;
import com.flash.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
