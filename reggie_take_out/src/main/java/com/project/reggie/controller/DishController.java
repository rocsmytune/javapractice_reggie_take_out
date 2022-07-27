package com.project.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.reggie.common.R;
import com.project.reggie.dto.DishDto;
import com.project.reggie.entity.Category;
import com.project.reggie.entity.Dish;
import com.project.reggie.entity.DishFlavor;
import com.project.reggie.service.CategoryService;
import com.project.reggie.service.DishFlavorService;
import com.project.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.util.List;
import java.util.stream.Collectors;

/***
 * menu management
 */
@RestController
@Slf4j
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private DishService dishService;

    @Autowired
    private CategoryService categoryService;

    /**
     * add new dish
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        log.info(dishDto.toString());

        dishService.saveWithFlavor(dishDto);

        return R.success("Add new dish success!");
    }

    /***
     * dish info paging
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        Page<Dish> pageInfo =  new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<Dish>();

        queryWrapper.like(name != null, Dish::getName, name);

        queryWrapper.orderByDesc(Dish::getUpdateTime);

        dishService.page(pageInfo, queryWrapper);

        //object copy
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");

        List<Dish> records = pageInfo.getRecords();

        List<DishDto> list =  records.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item, dishDto);

            Long categoryId = item.getCategoryId();
            //query object by id
            Category category = categoryService.getById(categoryId);

            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName((categoryName));
            }
            return dishDto;
        }).collect(Collectors.toList());


        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);

    }

    /**
     * get dish info by id
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id) {
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }

    /**
     * edit dish info
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {
        log.info(dishDto.toString());

        dishService.updateWithFlavor(dishDto);

        return R.success("Edit dish success!");
    }

    /**
     * query dish info according tp conditions in setmeal
     * @param dish
     * @return
     */
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish) {
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        queryWrapper.eq(Dish::getStatus, 1);

        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> list = dishService.list(queryWrapper);

        List<DishDto> dishDtolist =  list.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item, dishDto);

            Long categoryId = item.getCategoryId();
            //query object by id
            Category category = categoryService.getById(categoryId);

            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName((categoryName));
            }

            //当前菜品id
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(DishFlavor::getDishId, dishId);

            List<DishFlavor> dishFlavorList = dishFlavorService.list(queryWrapper1);
            dishDto.setFlavors(dishFlavorList);

            return dishDto;

        }).collect(Collectors.toList());

        return R.success(dishDtolist);
    }


}
