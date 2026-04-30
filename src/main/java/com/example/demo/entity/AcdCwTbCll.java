package com.example.demo.entity;

import java.time.LocalDate;
public class AcdCwTbCll {
    private LocalDate tjdate;
    private String comname;
    private Integer xzlDay;
    private Integer yjlDay;
    private Integer wjl;

    public LocalDate getTjdate() {
        return tjdate;
    }

    public void setTjdate(LocalDate tjdate) {   
        this.tjdate = tjdate;
    }


    public String getComname() {
        return comname;
    }

    public void setComname(String comname) {
        this.comname = comname;
    }

    public Integer getXzlDay() {
        return xzlDay;
    }

    public void setXzlDay(Integer xzlDay) {
        this.xzlDay = xzlDay;
    }

    public Integer getYjlDay() {
        return yjlDay;
    }

    public void setYjlDay(Integer yjlDay) {
        this.yjlDay = yjlDay;
    }

    public Integer getWjl() {
        return wjl;
    }

    public void setWjl(Integer wjl) {
        this.wjl = wjl;
    }
}
