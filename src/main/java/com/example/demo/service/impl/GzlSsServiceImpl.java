package com.example.demo.service.impl;

import com.example.demo.entity.GzlSs;
import com.example.demo.mapper.GzlSsMapper;
import com.example.demo.service.GzlSsService;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import java.util.List;

@Service
public class GzlSsServiceImpl implements GzlSsService {

    @Resource
    private GzlSsMapper gzlSsMapper;

    @Override
    public List<GzlSs> getList(String queryTime) {
        return gzlSsMapper.list(queryTime);
    }
}