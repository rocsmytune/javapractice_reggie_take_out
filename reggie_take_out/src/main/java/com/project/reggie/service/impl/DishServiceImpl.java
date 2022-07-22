package com.project.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.reggie.dto.DishDto;
import com.project.reggie.entity.Dish;
import com.project.reggie.entity.DishFlavor;
import com.project.reggie.mapper.DishMapper;
import com.project.reggie.service.DishFlavorService;
import com.project.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;

    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        //save basic info into dish table
        this.save(dishDto);

        Long dishId = dishDto.getId();

        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());

        //save flavor data into dish_flavor table
        dishFlavorService.saveBatch(flavors);
    }
    
    public DishDto getByIdWithFlavor(Long id) {
        //basic info
        Dish dish = this.getById(id);

        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);

        //flavor info : dish_flavor
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dish.getId());
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(flavors);

        return dishDto;
    }


    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        this.updateById(dishDto);

        //delete all and add all
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());

        dishFlavorService.remove(queryWrapper);

        //add
        List<DishFlavor> flavors = dishDto.getFlavors();

        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);


    }
}
