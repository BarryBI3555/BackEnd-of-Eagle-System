package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

@TableName("user_location")
public class UserLocation {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String usercode;
    private Double longitude;
    private Double latitude;
    @TableField("create_time")
    private LocalDateTime createTime;

    // 关联表字段
    @TableField(exist = false)
    private String username;
    @TableField(exist = false)
    private String groups;
    @TableField(exist = false)
    private String groupscode;
    @TableField(exist = false)
    private String ckl;

    @TableField(exist = false)
    private String dsl;
    @TableField(exist = false)
    private String hj;

    @TableField(exist = false)
    private String address;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsercode() {
        return usercode;
    }

    public void setUsercode(String usercode) {
        this.usercode = usercode;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCkl() {
        return ckl;
    }

    public void setCkl(String ckl) {
        this.ckl = ckl;
    }

    public String getDsl() {
        return dsl;
    }

    public void setDsl(String dsl) {
        this.dsl = dsl;
    }

    public String getHj() {
        return hj;
    }

    public void setHj(String hj) {
        this.hj = hj;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGroups() {
        return groups;
    }
    public void setGroups(String groups) {
        this.groups = groups;
    }

    public String getGroupscode() {
        return groupscode;
    }

    public void setGroupscode(String groupscode) {
        this.groupscode = groupscode;
    }
}