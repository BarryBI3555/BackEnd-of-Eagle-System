package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.entity.UserLocation;
import com.example.demo.mapper.UserLocationMapper;
import com.example.demo.service.UserLocationService;
import com.example.demo.util.LocationAddressConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserLocationServiceImpl implements UserLocationService {

    @Autowired
    private UserLocationMapper userLocationMapper;
    @Autowired
    private LocationAddressConverter addressConverter; // 地址解析工具

    // 用MyBatis-Plus自带的selectList实现，不用写SQL
    @Override
    public List<UserLocation> getAllLocations() {
        return userLocationMapper.selectList(new LambdaQueryWrapper<>());
    }


    // 调用自定义SQL方法
    @Override
    public List<UserLocation> getLatestLocationsByDate(LocalDate date, String groupscode, String keyword) {
        String dateStr = date.toString();
        List<UserLocation> list = userLocationMapper.getLatestLocationsByDate(dateStr, groupscode, keyword);
        addressConverter.convertBatch(list);
        return list;
    }

    // 获取指定用户当天轨迹（已自动转中文地址）
    @Override
    public List<UserLocation> getUserLocationsByDate(String usercode, LocalDate date) {
        String dateStr = date.toString();
        List<UserLocation> locationList = userLocationMapper.getUserLocationsByDate(usercode, dateStr);

        if (locationList == null || locationList.isEmpty()) {
            return locationList;
        }

        // ====================== 1. 按小时分组，取每小时最后一条（原来逻辑） ======================
        Map<String, UserLocation> hourlyMaxMap = locationList.stream()
                .collect(Collectors.toMap(
                        loc -> loc.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH")),
                        loc -> loc,
                        (existing, replacement) ->
                                existing.getCreateTime().isAfter(replacement.getCreateTime()) ? existing : replacement
                ));

        // ====================== 2. 找出当天最早一条（新增） ======================
        UserLocation firstLocationOfDay = locationList.stream()
                .min((a, b) -> a.getCreateTime().compareTo(b.getCreateTime()))
                .orElse(null);

        // ====================== 3. 把【最早一条】加入需要解析的列表 ======================
        List<UserLocation> needAddressList = new ArrayList<>(hourlyMaxMap.values());

        if (firstLocationOfDay != null) {
            // 避免重复：如果最早一条已经在每小时最后一条里，就不重复添加
            boolean alreadyIn = needAddressList.stream()
                    .anyMatch(loc -> loc.getId().equals(firstLocationOfDay.getId()));

            if (!alreadyIn) {
                needAddressList.add(firstLocationOfDay);
            }
        }

        // ====================== 4. 只对这些数据解析地址（省额度） ======================
        addressConverter.convertBatch(needAddressList);

        // 返回原列表（自动回填地址）
        return locationList;
    }

    
}