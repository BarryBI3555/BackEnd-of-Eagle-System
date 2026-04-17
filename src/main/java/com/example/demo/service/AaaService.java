package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.entity.Aaa;
import java.util.List;

public interface AaaService extends IService<Aaa> {
    List<Aaa> getAllData();
}