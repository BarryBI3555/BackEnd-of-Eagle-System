package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.entity.UserLocation;
import com.example.demo.mapper.UserLocationMapper;
import com.example.demo.service.UserLocationService;
import com.example.demo.util.LocationAddressConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

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
    public List<UserLocation> getLatestLocationsByDate(LocalDate date) {
        String dateStr = date.toString();
        List<UserLocation> list = userLocationMapper.getLatestLocationsByDate(dateStr);

        // 自动转换经纬度 → 中文地址
        addressConverter.convertBatch(list);

        return list;
    }

    // 获取指定用户当天轨迹（已自动转中文地址）
    @Override
    public List<UserLocation> getUserLocationsByDate(String usercode, LocalDate date) {
        String dateStr = date.toString();
        List<UserLocation> list = userLocationMapper.getUserLocationsByDate(usercode, dateStr);

        // 轨迹也自动转地址（前端显示轨迹非常有用）
        addressConverter.convertBatch(list);

        return list;
    }
}