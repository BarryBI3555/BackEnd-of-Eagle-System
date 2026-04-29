package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.PrplCheckTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PrplCheckTaskMapper extends BaseMapper<PrplCheckTask> {
    List<PrplCheckTask> getAllTasksByDate(@Param("dateStr") String dateStr);
}
