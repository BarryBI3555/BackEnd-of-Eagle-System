package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.LocationCache;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDateTime;

public interface LocationCacheMapper extends BaseMapper<LocationCache> {
    LocationCache findByCoordinates(@Param("longitude") Double longitude, @Param("latitude") Double latitude);
    LocationCache findValidByCoordinates(@Param("longitude") Double longitude, @Param("latitude") Double latitude, @Param("currentTime") String currentTime);
}