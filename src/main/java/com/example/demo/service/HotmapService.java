package com.example.demo.service;

import com.example.demo.entity.HeatData;
import java.time.LocalDate;
import java.util.List;

public interface HotmapService {
    /**
     * 获取热力图数据（从prplchecktask表获取）
     * @param date 日期
     * @return 热力图数据列表
     */
    List<HeatData> getHeatData(LocalDate date);
}
