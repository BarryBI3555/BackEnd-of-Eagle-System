package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.Aaa;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AaaMapper extends BaseMapper<Aaa> {
    // 继承BaseMapper，自带所有CRUD方法，不用写SQL
}