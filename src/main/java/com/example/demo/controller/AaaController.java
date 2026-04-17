package com.example.demo.controller;

import com.example.demo.entity.Aaa;
import com.example.demo.service.AaaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/aaa")
@CrossOrigin("*") // 允许前端跨域访问
public class AaaController {

    @Autowired
    private AaaService aaaService;

    /**
     * 查询aaa表所有数据
     * 接口地址：GET http://localhost:8080/api/aaa/list
     */
    @GetMapping("/list")
    public List<Aaa> getList() {
        return aaaService.getAllData();
    }
}