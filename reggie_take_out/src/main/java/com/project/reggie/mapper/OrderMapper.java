package com.project.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.project.reggie.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Orders> {
}
