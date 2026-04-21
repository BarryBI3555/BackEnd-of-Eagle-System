package com.example.demo.controller;


import com.example.demo.entity.UserLocation;
import com.example.demo.mapper.GroupMapper;
import com.example.demo.service.UserLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDate;
import java.util.*;


@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class LocationController {

    @Autowired
    private UserLocationService userLocationService;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${tencent.map.key}")
    private String tencentMapKey;


    @GetMapping("/locations")
    public List<UserLocation> getLocations() {
        return userLocationService.getAllLocations();
    }

    // ====================== 获取指定日期每个用户最新位置 ======================
    @GetMapping("/locations/latest")
    public List<UserLocation> getLatestLocations(
            @RequestParam(required = false) String date
    ) {
        LocalDate queryDate = date == null ? LocalDate.now() : LocalDate.parse(date);
        return userLocationService.getLatestLocationsByDate(queryDate);
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

    // ====================== 腾讯地图获取行政区划json ======================
    
    // 地理位置逆解析
    @GetMapping("/map/geocoder")
    public Object getGeocoder(@RequestParam String location) {
        try {
            String url = "https://apis.map.qq.com/ws/geocoder/v1/?location=" + location + "&key=" + tencentMapKey;
            return restTemplate.getForObject(url, Object.class);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", 500);
            error.put("message", "请求失败: " + e.getMessage());
            return error;
        }
    }

    //获取城市名称
    @GetMapping("/map/district/search")
    public Object getDistrictname(@RequestParam(required = false) String keyword) {
        try {
            // 参数校验
            if (keyword == null || keyword.trim().isEmpty() || keyword.equals("undefined")) {
                Map<String, Object> error = new HashMap<>();
                error.put("status", 400);
                error.put("message", "搜索关键词不能为空");
                return error;
            }
            
            String url = "https://apis.map.qq.com/ws/district/v1/search?key=" + tencentMapKey + "&keyword=" + keyword + "&get_polygon=2&max_offset=100";
            return restTemplate.getForObject(url, Object.class);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", 500);
            error.put("message", "请求失败: " + e.getMessage());
            return error;
        }
    }


    // 获取下级行政区划
    @GetMapping("/map/district/getchildren")
    public Object getDistrictChildren(@RequestParam(required = false) String id) {
        try {
            // 参数校验
            if (id == null || id.trim().isEmpty() || id.equals("undefined")) {
                Map<String, Object> error = new HashMap<>();
                error.put("status", 400);
                error.put("message", "行政区划ID不能为空");
                return error;
            }
            
            String url = "https://apis.map.qq.com/ws/district/v1/getchildren?key=" + tencentMapKey + "&id=" + id + "&get_polygon=2&max_offset=1000";
            return restTemplate.getForObject(url, Object.class);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", 500);
            error.put("message", "请求失败: " + e.getMessage());
            return error;
        }
    }

    

}
