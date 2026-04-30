package com.example.demo.service.impl;

import com.example.demo.entity.AcdCwTbCll;
import com.example.demo.entity.GeocodeResult;
import com.example.demo.entity.HeatData;
import com.example.demo.entity.PrplCheckTask;
import com.example.demo.entity.StatsCardData;
import com.example.demo.mapper.AcdCwTbCllMapper;
import com.example.demo.mapper.PrplCheckTaskMapper;
import com.example.demo.service.HotmapService;
import com.example.demo.util.LocationAddressConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HotmapServiceImpl implements HotmapService {

    private static final Logger logger = LoggerFactory.getLogger(HotmapServiceImpl.class);

    @Autowired
    private PrplCheckTaskMapper prplCheckTaskMapper;

    @Autowired
    private AcdCwTbCllMapper acdCwTbCllMapper;

    @Autowired
    private LocationAddressConverter addressConverter;

    @Override
    public List<HeatData> getHeatData(LocalDate date) {
        String dateStr = date.toString();
        // 从carlpdb.prplchecktask表获取指定日期的数据
        List<PrplCheckTask> tasks = prplCheckTaskMapper.getAllTasksByDate(dateStr);

        if (tasks == null || tasks.isEmpty()) {
            logger.warn("未找到指定日期 {} 的检查任务数据", dateStr);
            return new ArrayList<>();
        }

        logger.info("获取到 {} 条检查任务数据", tasks.size());

        // 统计每个经纬度出现的次数
        Map<String, Integer> coordCountMap = new HashMap<>();

        for (PrplCheckTask task : tasks) {
            Double lng = task.getChecklongitude();
            Double lat = task.getChecklatitude();
            String checksite = task.getChecksite();

            // 如果经纬度为空，尝试通过地址解析获取
            if (lng == null || lat == null || Double.isNaN(lng) || Double.isNaN(lat)) {
                if (checksite != null && !checksite.trim().isEmpty()) {
                    logger.debug("经纬度为空，尝试通过地址解析: {}", checksite);
                    GeocodeResult result = addressConverter.geocode(checksite);
                    if (result != null && result.getStatus() == 0 && result.getResult() != null
                            && result.getResult().getLocation() != null) {
                        lng = result.getResult().getLocation().getLng();
                        lat = result.getResult().getLocation().getLat();
                        logger.debug("地址解析成功: {} -> lng={}, lat={}", checksite, lng, lat);
                    } else {
                        logger.warn("地址解析失败: {}", checksite);
                        continue;
                    }
                } else {
                    logger.warn("经纬度和地址都为空，跳过记录");
                    continue;
                }
            }

            // 使用经纬度作为key（保留4位小数）
            String coordKey = String.format("%.4f,%.4f", lng, lat);
            coordCountMap.merge(coordKey, 1, Integer::sum);
        }

        logger.info("统计到 {} 个唯一坐标点", coordCountMap.size());

        // 转换为HeatData列表
        List<HeatData> heatDataList = new ArrayList<>();
        for (Map.Entry<String, Integer> coordEntry : coordCountMap.entrySet()) {
            String[] coords = coordEntry.getKey().split(",");
            HeatData heatData = new HeatData();
            heatData.setLng(Double.parseDouble(coords[0]));
            heatData.setLat(Double.parseDouble(coords[1]));
            heatData.setCount(coordEntry.getValue());
            heatDataList.add(heatData);
        }

        return heatDataList;
    }

    @Override
    public List<StatsCardData> getStatsCardsData(LocalDate date){
        List<StatsCardData> result = new ArrayList<>();

        try {
            // 查询统计数据
                AcdCwTbCll data = acdCwTbCllMapper.selectByDate(date);

            if (data != null) {
                logger.info("获取统计数据成功: date={}", 
                        date);

                // 当日新增立案量
                result.add(new StatsCardData("新增立案", 
                        data.getXzlDay() != null ? data.getXzlDay() : 0, 
                        "当日新增立案量"));

                // 当日已决量
                result.add(new StatsCardData("已决案件", 
                        data.getYjlDay() != null ? data.getYjlDay() : 0, 
                        "当日已决量"));

                // 未决量
                result.add(new StatsCardData("未决案件", 
                        data.getWjl() != null ? data.getWjl() : 0, 
                        "截止统计日期未决量"));
                } else {
                logger.warn("未找到统计数据: date={}", date);
                
                // 返回默认空数据
                result.add(new StatsCardData("新增立案", 0, "当日新增立案量"));
                result.add(new StatsCardData("已决案件", 0, "当日已决量"));
                result.add(new StatsCardData("未决案件", 0, "截止统计日期未决量"));
            }
        } catch (Exception e) {
            logger.error("获取统计卡片数据失败: date={}, {}", 
                        date, e.getMessage());
            
            // 返回默认空数据
            result.add(new StatsCardData("新增立案", 0, "当日新增立案量"));
            result.add(new StatsCardData("已决案件", 0, "当日已决量"));
            result.add(new StatsCardData("未决案件", 0, "截止统计日期未决量"));
        }

        return result;
    }
}
