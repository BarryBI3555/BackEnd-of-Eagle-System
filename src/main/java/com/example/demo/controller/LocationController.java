package com.example.demo.controller;  // 注意改成你的包名！

import com.example.demo.dao.UserLocationDao;
import com.example.demo.entity.UserLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;


@CrossOrigin
@RestController
@RequestMapping("/api")
public class LocationController {

    @Autowired
    private UserLocationDao userLocationDao;

    @GetMapping("/locations")
    public List<UserLocation> getLocations() {
        return userLocationDao.getAllLocations();
    }
}