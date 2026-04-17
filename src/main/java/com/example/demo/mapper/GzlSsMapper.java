package com.example.demo.mapper;

import com.example.demo.entity.GzlSs;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface GzlSsMapper {
    // 查询 gzl_ss 表数据（支持按日期筛选）
    List<GzlSs> list(@Param("queryTime") String queryTime);
}