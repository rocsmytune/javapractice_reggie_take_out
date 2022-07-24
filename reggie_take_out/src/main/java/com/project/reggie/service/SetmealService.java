package com.project.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.project.reggie.dto.SetmealDto;
import com.project.reggie.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    /***
     * add new meal and save relations between meal and dishes at the same time
     * @param setmealDto
     */
    public void saveWithDish(SetmealDto setmealDto);
    public void removeWithDish(List<Long> ids);
}
