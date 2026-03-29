package com.example.demo.controller;  // 注意改成你的包名！

import com.example.demo.dao.UserLocationDao;
import com.example.demo.entity.UserLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;


@CrossOrigin
@RestController
@RequestMapping("/api")
public class LocationController {

    @Autowired
    private UserLocationDao userLocationDao;

    @GetMapping("/locations")
    public List<UserLocation> getLocations() {
        return userLocationDao.getAllLocations();
    }

    // 获取每个用户最新时间的经纬度
    @GetMapping("/locations/latest")
    public List<UserLocation> getLatestLocations() {
        return userLocationDao.getLatestLocationsByUser();
    }

    // 获取单个用户当天全部的历史轨迹数据
    @GetMapping("/locations/user/{usercode}")
    public List<UserLocation> getTodayLocationsByUser(@PathVariable String usercode) {
        return userLocationDao.getTodayLocationsByUser(usercode);
    }
}