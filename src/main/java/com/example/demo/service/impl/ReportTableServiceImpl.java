package com.example.demo.service.impl;

import com.example.demo.entity.CurGzlTableRy;
import com.example.demo.entity.CurGzlTableBm;
import com.example.demo.entity.CurGzlTableGroup;
import com.example.demo.entity.CurGzlTableRs;
import com.example.demo.mapper.ReportTableMapper;
import com.example.demo.service.ReportTableService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

@Service
public class ReportTableServiceImpl implements ReportTableService {

    @Resource
    private ReportTableMapper reportTableMapper;

    // 通用
    @Override
    public String getMaxTjDate(String tableName) {
        return reportTableMapper.getMaxTjDateByTable(tableName);
    }

    @Override
    public List<CurGzlTableRy> getCurGzlData(String startDate, String endDate, String comName, String groups, String userName) {
        return reportTableMapper.getCurGzlData(startDate, endDate, comName, groups, userName);
    }

    @Override
    public List<CurGzlTableBm> getCurGzlDataBm(String startDate, String endDate, String comName) {
        return reportTableMapper.getCurGzlDataBm(startDate, endDate, comName);
    }

    @Override
    public List<CurGzlTableGroup> getCurGzlDataGroup(String startDate, String endDate, String comName, String groups) {
        return reportTableMapper.getCurGzlDataGroup(startDate, endDate, comName, groups);
    }

    @Override
    public List<CurGzlTableRs> getCurGzlDataRs(String startDate, String endDate, String comName) {
        return reportTableMapper.getCurGzlDataRs(startDate, endDate, comName);
    }
}
