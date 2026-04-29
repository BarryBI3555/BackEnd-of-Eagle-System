package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("prplchecktask")
public class PrplCheckTask {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Double checklongitude;
    private Double checklatitude;
    private String checkdate;
    private String checksite;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getChecklongitude() {
        return checklongitude;
    }

    public void setChecklongitude(Double checklongitude) {
        this.checklongitude = checklongitude;
    }

    public Double getChecklatitude() {
        return checklatitude;
    }

    public void setChecklatitude(Double checklatitude) {
        this.checklatitude = checklatitude;
    }

    public String getCheckdate() {
        return checkdate;
    }

    public void setCheckdate(String checkdate) {
        this.checkdate = checkdate;
    }

    public String getChecksite() {
        return checksite;
    }

    public void setChecksite(String checksite) {
        this.checksite = checksite;
    }
}
