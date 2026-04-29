package com.example.demo.controller;

import com.example.demo.entity.HeatData;
import com.example.demo.service.HotmapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class HotmapController {

    @Autowired
    private HotmapService hotmapService;

    // ====================== 热力图数据接口 ======================
    @GetMapping("/hotmap")
    public List<HeatData> getHotmapData(
            @RequestParam(required = false) String date
    ) {
        LocalDate queryDate = date == null ? LocalDate.now() : LocalDate.parse(date);
        return hotmapService.getHeatData(queryDate);
    }
}
