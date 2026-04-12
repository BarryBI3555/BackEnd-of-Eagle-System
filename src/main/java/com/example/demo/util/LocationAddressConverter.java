package com.example.demo.util;

import com.example.demo.entity.UserLocation;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.lang.Thread;
import java.util.List;

@Component
public class LocationAddressConverter {

    // 读取你配置的腾讯KEY
    @Value("${tencent.map.key}")
    private String tencentMapKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<UserLocation> convertBatch(List<UserLocation> locationList) {
        if (locationList == null || locationList.isEmpty()) return locationList;

        for (UserLocation loc : locationList) {
            try {
                Double lng = loc.getLongitude();
                Double lat = loc.getLatitude();

                if (lng == null || lat == null) {
                    loc.setAddress("坐标无效");
                    continue;
                }

                String url = "https://apis.map.qq.com/ws/geocoder/v1/"
                        + "?location=" + lat + "," + lng
                        + "&key=" + tencentMapKey;

                // 最简单、最干净的请求
                String jsonResp = restTemplate.getForObject(url, String.class);
                JsonNode root = objectMapper.readTree(jsonResp);

                if (root.path("status").asInt() == 0) {
                    loc.setAddress(root.path("result").path("address").asText());
                } else {
                    loc.setAddress(root.path("message").asText());
                }

                Thread.sleep(300); // 延迟300毫秒，防止每秒请求超限

            } catch (Exception e) {
                loc.setAddress("解析异常");
                e.printStackTrace();
            }
        }
        return locationList;
    }
}