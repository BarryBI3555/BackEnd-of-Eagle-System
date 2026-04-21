package com.example.demo.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.entity.Group;
import com.example.demo.mapper.GroupMapper;

import java.util.stream.Collectors;
@RestController
@RequestMapping("/api")
public class GroupController {
    
    @Autowired
    private GroupMapper groupMapper;

    @GetMapping("/locations/groups")
    
    public List<Group> getGroups() {
        List<Group> allGroups = groupMapper.selectAllGroups();
        // 根据groupscode去重
        Map<String, Group> groupMap = new LinkedHashMap<>();
        for (Group group : allGroups) {
            if (group.getGroupscode() != null && !group.getGroupscode().isEmpty()) {
                groupMap.put(group.getGroupscode(), group);
            }
        }
        return new ArrayList<>(groupMap.values());
    }
}
