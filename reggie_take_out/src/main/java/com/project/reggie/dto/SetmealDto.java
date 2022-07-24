package com.project.reggie.dto;

import com.project.reggie.entity.Setmeal;
import com.project.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
