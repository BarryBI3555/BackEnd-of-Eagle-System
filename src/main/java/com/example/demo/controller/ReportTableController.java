package com.example.demo.controller;

import com.example.demo.entity.CurGzlTableRy;
import com.example.demo.entity.CurGzlTableBm;
import com.example.demo.entity.Result;
import com.example.demo.service.ReportTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class ReportTableController {

    @Autowired
    private ReportTableService reportTableService;

    @GetMapping("/cur_gzl/list")
    public Result<List<CurGzlTableRy>> getCurGzlList(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String comName,
            @RequestParam(required = false) String groups,
            @RequestParam(required = false) String userName
    ) {
        try {
            // 日期都为空 → 取最大日期
            if ((startDate == null || startDate.trim().isEmpty())
                    && (endDate == null || endDate.trim().isEmpty())) {

                // 直接调用service
                String maxDate = reportTableService.getMaxTjDate();
                startDate = maxDate;
                endDate = maxDate;
            }

            // 只有开始日期
            if (endDate == null || endDate.trim().isEmpty()) {
                endDate = startDate;
            }

            List<CurGzlTableRy> data = reportTableService.getCurGzlData(
                    startDate, endDate, comName, groups, userName);

            return Result.success(data);

        } catch (Exception e) {
            return Result.error("获取失败：" + e.getMessage());
        }
    }

    @GetMapping("/cur_gzl_bm/list")
    public Result<List<CurGzlTableBm>> getCurGzlListBm(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String comName
    ) {
        try {
            if ((startDate == null || startDate.trim().isEmpty())
                    && (endDate == null || endDate.trim().isEmpty())) {
                String maxDate = reportTableService.getMaxTjDateBm();
                startDate = maxDate;
                endDate = maxDate;
            }

            if (endDate == null || endDate.trim().isEmpty()) {
                endDate = startDate;
            }

            List<CurGzlTableBm> data = reportTableService.getCurGzlDataBm(
                    startDate, endDate, comName);

            return Result.success(data);

        } catch (Exception e) {
            return Result.error("获取部门统计失败：" + e.getMessage());
        }
    }
}