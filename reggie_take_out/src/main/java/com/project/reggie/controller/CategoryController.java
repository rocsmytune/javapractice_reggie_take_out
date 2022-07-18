package com.project.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.reggie.common.R;
import com.project.reggie.entity.Category;
import com.project.reggie.entity.Employee;
import com.project.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


/**
 * Category Management
 */
@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /***
     * Add new category
     * @param category
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Category category) {
        log.info("category:{}", category);
        categoryService.save(category);
        return R.success("Add new category success!");
    }

    /***
     * category paging query based on MybatisPlus
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize) {
        log.info("Category ： page = {}, pageSize = {}", page, pageSize);

        //construct paging struct
        Page<Category> pageInfo = new Page<>(page, pageSize);
        //construct condition struct
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //add order condition
        queryWrapper.orderByAsc(Category::getSort);

        //run query
        categoryService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    /***
     * delete category by id
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(Long ids) {
        log.info("Delete Category ID: {}", ids);

        categoryService.remove(ids);
        return R.success("Category info delete success!");
    }

    /***
     * edit category info acccording to id
     * @param category
     * @return
     */
    @PutMapping
    public  R<String> update(@RequestBody Category category) {
        log.info(category.toString());

        categoryService.updateById(category);
        
        return R.success("Update Category Info Success!");
    }
}
