package com.example.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.Aaa;
import com.example.demo.mapper.AaaMapper;
import com.example.demo.service.AaaService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AaaServiceImpl extends ServiceImpl<AaaMapper, Aaa> implements AaaService {

    @Override
    public List<Aaa> getAllData() {
        return this.list(); // 查询aaa表所有数据
    }
}