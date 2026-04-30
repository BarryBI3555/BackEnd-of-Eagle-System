package com.example.demo.mapper;

import com.example.demo.entity.AcdCwTbCll;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDate;
@Mapper
public interface AcdCwTbCllMapper {
    AcdCwTbCll selectByDate(@Param("date") LocalDate date);
}
