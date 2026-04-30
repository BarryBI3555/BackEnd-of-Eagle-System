package com.example.demo.entity;

public class StatsCardData {
    private String title;
    private Integer count;
    private String description;

    public StatsCardData() {}

    public StatsCardData(String title, Integer count, String description) {
        this.title = title;
        this.count = count;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
