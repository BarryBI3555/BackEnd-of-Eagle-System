package com.example.demo.service;

public interface LocationCacheWarmupService {
    /**
     * 预热位置缓存
     * 每小时自动执行一次，获取当天最新位置并解析地址
     */
    void warmupLocationCache();
}