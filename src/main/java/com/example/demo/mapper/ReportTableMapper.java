package com.example.demo.mapper;

import com.example.demo.entity.CurGzlTableRy;
import com.example.demo.entity.CurGzlTableBm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;


@Mapper
public interface ReportTableMapper{
    // 继承BaseMapper，自带所有CRUD方法，不用写SQL
    List<CurGzlTableRy> getCurGzlData(@Param("startDate") String startDate,
                                      @Param("endDate") String endDate,
                                      @Param("comName") String comName,
                                      @Param("groups") String groups,
                                      @Param("userName") String userName);

    String getMaxTjDate();

    List<CurGzlTableBm> getCurGzlDataBm(
            @Param("startDate") String startDate,
            @Param("endDate") String endDate,
            @Param("comName") String comName
    );

    String getMaxTjDateBm();
}