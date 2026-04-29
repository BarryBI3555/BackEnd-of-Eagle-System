package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class AdministrativeRegionController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${tencent.map.key}")
    private String tencentMapKey;

    @Value("${tencent.map.domain}")
    private String mapDomain;

    @Value("${tencent.map.api-domain}")
    private String apiDomain;

    // 地理位置逆解析
    @GetMapping("/map/geocoder")
    public Object getGeocoder(@RequestParam String location) {
        try {
            String url = apiDomain + "ws/geocoder/v1/?location=" + location + "&key=" + tencentMapKey;
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            
            // 尝试解析为JSON，失败则返回原始字符串
            return parseResponse(response.getBody());
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", 500);
            error.put("message", "请求失败: " + e.getMessage());
            return error;
        }
    }

    // 获取城市名称
    @GetMapping("/map/district/search")
    public Object getDistrictname(@RequestParam(required = false) String keyword) {
        try {
            if (keyword == null || keyword.trim().isEmpty() || keyword.equals("undefined")) {
                Map<String, Object> error = new HashMap<>();
                error.put("status", 400);
                error.put("message", "搜索关键词不能为空");
                return error;
            }
            
            String url = apiDomain + "ws/district/v1/search?key=" + tencentMapKey + "&keyword=" + keyword;
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            
            return parseResponse(response.getBody());
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
            if (id == null || id.trim().isEmpty() || id.equals("undefined")) {
                Map<String, Object> error = new HashMap<>();
                error.put("status", 400);
                error.put("message", "行政区划ID不能为空");
                return error;
            }
            
            String url = apiDomain + "ws/district/v1/getchildren?key=" + tencentMapKey + "&id=" + id + "&get_polygon=2&max_offset=100";
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            
            return parseResponse(response.getBody());
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", 500);
            error.put("message", "请求失败: " + e.getMessage());
            return error;
        }
    }
    
    /**
     * 解析响应内容，尝试转换为JSON对象
     */
    private Object parseResponse(String responseBody) {
        if (responseBody == null) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", 500);
            error.put("message", "响应内容为空");
            return error;
        }
        
        // 检查是否为JSON格式
        if (responseBody.trim().startsWith("{")) {
            try {
                // 使用Jackson解析JSON
                com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                return mapper.readValue(responseBody, Object.class);
            } catch (Exception e) {
                // 解析失败，返回原始字符串
                return responseBody;
            }
        } else {
            // 不是JSON格式，返回错误信息
            Map<String, Object> error = new HashMap<>();
            error.put("status", 500);
            error.put("message", "API返回非JSON格式数据");
            error.put("rawResponse", responseBody);
            return error;
        }
    }
}
