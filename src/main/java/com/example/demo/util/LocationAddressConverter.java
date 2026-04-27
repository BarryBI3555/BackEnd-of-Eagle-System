package com.example.demo.util;

import com.example.demo.entity.LocationCache;
import com.example.demo.entity.UserLocation;
import com.example.demo.mapper.LocationCacheMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.lang.Thread;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class LocationAddressConverter {

    // 读取你配置的腾讯KEY
    @Value("${tencent.map.key}")
    private String tencentMapKey;
    @Value("${tencent.map.api-domain}")
    private String tencentMapDomain;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final LocationCacheMapper locationCacheMapper;

    public LocationAddressConverter(LocationCacheMapper locationCacheMapper) {
        this.locationCacheMapper = locationCacheMapper;
    }

    public List<UserLocation> convertBatch(List<UserLocation> locationList) {
        if (locationList == null || locationList.isEmpty()) return locationList;

        for (UserLocation loc : locationList) {
            try {
                Double lng = loc.getLongitude();
                Double lat = loc.getLatitude();

                if (lng == null || lat == null || Double.isNaN(lng) || Double.isNaN(lat)) {
                    loc.setAddress("坐标无效");
                    continue;
                }

                // 验证坐标范围（中国地区大致范围）
                if (lng < 73 || lng > 135 || lat < 3 || lat > 53) {
                    loc.setAddress("坐标范围无效");
                    continue;
                }

                // 先查询数据库缓存
                LocationCache cache = locationCacheMapper.findByCoordinates(lng, lat);
                if (cache != null) {
                    loc.setAddress(cache.getAddress());
                    continue;
                }

                String url = tencentMapDomain + "/ws/geocoder/v1/"
                        + "?location=" + lat + "," + lng
                        + "&key=" + tencentMapKey;

                // 最简单、最干净的请求
                String jsonResp = restTemplate.getForObject(url, String.class);
                JsonNode root = objectMapper.readTree(jsonResp);

                String address;
                if (root.path("status").asInt() == 0) {
                    address = root.path("result").path("address").asText();
                    // 存入缓存
                    saveToCache(lng, lat, address);
                } else {
                    address = root.path("message").asText();
                }
                loc.setAddress(address);

                    Thread.sleep(300); // 延迟300毫秒，防止每秒请求超限
                
                } catch (Exception e) {
                loc.setAddress("解析异常");
                e.printStackTrace();
                }
            }
        return locationList;
    }

    private void saveToCache(Double longitude, Double latitude, String address) {
        LocationCache cache = new LocationCache();
        cache.setLongitude(longitude);
        cache.setLatitude(latitude);
        cache.setAddress(address);
        cache.setCreateTime(LocalDateTime.now());
        cache.setUpdateTime(LocalDateTime.now());
        locationCacheMapper.insert(cache);
    }
}