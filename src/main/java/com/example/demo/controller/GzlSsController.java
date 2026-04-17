package com.example.demo.controller;


import com.example.demo.entity.GzlSs;
import com.example.demo.service.GzlSsService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jakarta.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class GzlSsController {

    @Resource
    private GzlSsService gzlSsService;

    // ====================== 获取指定时间所有数据 ======================
    @GetMapping("/gzlSs/list")
    public Map<String, Object> list(
            @RequestParam(value = "queryTime", required = false) String queryTime
    ) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<GzlSs> data = gzlSsService.getList(queryTime);
            result.put("code", 200);
            result.put("data", data);
            result.put("msg", "查询成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "查询失败");
        }
        return result;
    }
}