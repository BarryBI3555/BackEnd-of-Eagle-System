package com.example.demo.service;

import com.example.demo.entity.HeatData;
import com.example.demo.entity.StatsCardData;
import java.time.LocalDate;
import java.util.List;

public interface HotmapService {
    /**
     * 获取热力图数据（从prplchecktask表获取）
     * @param date 日期
     * @return 热力图数据列表
     */
    List<HeatData> getHeatData(LocalDate date);

    /**
     * 获取统计卡片数据
     * @param date 统计日期
     * @return 统计卡片数据列表
     */
    List<StatsCardData> getStatsCardsData(LocalDate date);
}
