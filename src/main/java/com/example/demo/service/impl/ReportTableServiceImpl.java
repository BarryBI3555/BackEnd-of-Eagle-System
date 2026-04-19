package com.example.demo.service.impl;

import com.example.demo.entity.CurGzlTableRy;
import com.example.demo.entity.CurGzlTableBm;
import com.example.demo.mapper.ReportTableMapper;
import com.example.demo.service.ReportTableService;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import java.util.List;

@Service
public class ReportTableServiceImpl implements ReportTableService {

    @Resource
    private ReportTableMapper reportTableMapper;

    @Override
    public List<CurGzlTableRy> getCurGzlData(String startDate, String endDate, String comName, String groups, String userName) {
        return reportTableMapper.getCurGzlData(startDate, endDate, comName, groups, userName);
    }

    @Override
    public String getMaxTjDate() {
        return reportTableMapper.getMaxTjDate();
    }

    @Override
    public List<CurGzlTableBm> getCurGzlDataBm(String startDate, String endDate, String comName) {
        return reportTableMapper.getCurGzlDataBm(startDate, endDate, comName);
    }

    @Override
    public String getMaxTjDateBm() {
        return reportTableMapper.getMaxTjDateBm();
    }

}
