package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;

@Data
@TableName("aaa")
public class Aaa {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String cityCompany;                // 市公司
    private BigDecimal surveyCycle;            // 查勘周期
    private BigDecimal urgeDetermineCycle;     // 催定周期
    private BigDecimal determineCycle;        // 定损周期
    private BigDecimal determineToPay;        // 定损完成-支付
    private BigDecimal totalCloseCycle;       // 整体结案周期
    private BigDecimal under10kCloseCycle;     // 万元内结案周期
    private BigDecimal over10kCloseCycle;      // 万元以上结案周期
}