package com.example.demo.controller;


import com.example.demo.entity.UserLocation;
import com.example.demo.service.UserLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;
import java.util.List;


@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class LocationController {

    @Autowired
    private UserLocationService userLocationService;

    @GetMapping("/locations")
    public List<UserLocation> getLocations() {
        return userLocationService.getAllLocations();
    }

    // ====================== 获取指定日期每个用户最新位置 ======================
    @GetMapping("/locations/latest")
    public List<UserLocation> getLatestLocations(
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String groupscode,
            @RequestParam(required = false) String keyword
    ) {
        LocalDate queryDate = date == null ? LocalDate.now() : LocalDate.parse(date);
        return userLocationService.getLatestLocationsByDate(queryDate, groupscode, keyword);
    }

    // ====================== 获取指定日期用户当天轨迹 ======================
    @GetMapping("/locations/user/{usercode}")
    public List<UserLocation> getTodayLocationsByUser(
            @PathVariable String usercode,
            @RequestParam(required = false) String date
    ) {
        LocalDate queryDate = date == null ? LocalDate.now() : LocalDate.parse(date);
        return userLocationService.getUserLocationsByDate(usercode, queryDate);
    }

}
