package com.example.demo.service;

import com.example.demo.entity.CurGzlTableRy;
import com.example.demo.entity.CurGzlTableBm;
import java.util.List;

public interface ReportTableService {
    List<CurGzlTableRy> getCurGzlData(String startDate, String endDate, String comName, String groups, String userName);

    String getMaxTjDate();

    // 新增：按部门统计
    List<CurGzlTableBm> getCurGzlDataBm(String startDate, String endDate, String comName);

    // 新增：获取最大日期（可共用）
    String getMaxTjDateBm();
}