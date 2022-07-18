package com.project.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.reggie.common.CustomException;
import com.project.reggie.entity.Category;
import com.project.reggie.entity.Dish;
import com.project.reggie.entity.Setmeal;
import com.project.reggie.mapper.CategoryMapper;
import com.project.reggie.service.CategoryService;
import com.project.reggie.service.DishService;
import com.project.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService{

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;
    /***
     * delete category by id and do judgement
     * @param id
     */
    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<Dish>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        int countD = dishService.count(dishLambdaQueryWrapper);
        if(countD > 0) {
            throw new CustomException("Reject Delete : Current category has related dish");
        }


        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<Setmeal>();
        //add query condition
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        int countS = setmealService.count(setmealLambdaQueryWrapper);
        //query if current category relate to dish/meal
        if(countS > 0) {
            throw new CustomException("Reject Delete : Current category has related setmeal");
        }

        //run delete
        super.removeById(id);
    }
}
