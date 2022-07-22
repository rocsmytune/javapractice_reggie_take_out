package com.project.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.project.reggie.dto.DishDto;
import com.project.reggie.entity.Dish;

public interface DishService extends IService<Dish> {

//    add dish and flavor at the same time
    public void saveWithFlavor(DishDto dishDto);
}
