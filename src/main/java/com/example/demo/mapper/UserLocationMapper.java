package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.UserLocation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDate;
import java.util.List;

// @Mapper注解，标记为MyBatis Mapper
@Mapper
public interface UserLocationMapper extends BaseMapper<UserLocation> {

    // ====================== 获取指定日期每个用户最新位置（含姓名、ckl、dsl、hj） ======================
    List<UserLocation> getLatestLocationsByDate(
            @Param("dateStr") String dateStr,
            @Param("groupscode") String groupscode,
            @Param("keyword") String keyword
    );

    // ====================== 获取指定日期用户轨迹（也返回姓名） ======================
    List<UserLocation> getUserLocationsByDate(@Param("usercode") String usercode, @Param("dateStr") String dateStr);

    // ====================== 获取指定日期所有位置按时间排序 ======================
    // List<UserLocation> getAllLocationsOrderByTime(@Param("dateStr") String dateStr);
}