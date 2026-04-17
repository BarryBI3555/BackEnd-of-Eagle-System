package com.example.demo.service;

import com.example.demo.entity.GzlSs;
import java.util.List;

public interface GzlSsService {
    List<GzlSs> getList(String queryTime);
}