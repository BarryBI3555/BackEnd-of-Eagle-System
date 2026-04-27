package com.example.demo.service.impl;

import com.example.demo.entity.UserLocation;
import com.example.demo.mapper.UserLocationMapper;
import com.example.demo.service.LocationCacheWarmupService;
import com.example.demo.util.LocationAddressConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class LocationCacheWarmupServiceImpl implements LocationCacheWarmupService {

    @Autowired
    private UserLocationMapper userLocationMapper;

    @Autowired
    private LocationAddressConverter addressConverter;

    @Override
    @Scheduled(cron = "0 3 9-20 * * ?")
    public void warmupLocationCache() {
        String dateStr = LocalDate.now().toString();
        List<UserLocation> locationList = userLocationMapper.getLatestLocationsByDate(dateStr, null, null);

        if (locationList != null && !locationList.isEmpty()) {
            addressConverter.convertBatch(locationList);
        }
    }
}