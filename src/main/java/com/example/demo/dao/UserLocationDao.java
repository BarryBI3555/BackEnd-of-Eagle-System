package com.example.demo.dao;  // 注意改成你的包名！

import com.example.demo.entity.UserLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
public class UserLocationDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<UserLocation> getAllLocations() {
        String sql = "SELECT id, usercode, longitude, latitude, create_time FROM user_location";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(UserLocation.class));
    }

    // 获取每个用户最新时间的经纬度
    public List<UserLocation> getLatestLocationsByUser() {
        String sql = "SELECT id, usercode, longitude, latitude, create_time FROM user_location " +
                "WHERE (usercode, create_time) IN " +
                "(SELECT usercode, MAX(create_time) FROM user_location GROUP BY usercode)";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(UserLocation.class));
    }

    // 获取单个用户当天全部的历史轨迹数据
    /*
    public List<UserLocation> getTodayLocationsByUser(String usercode) {
        LocalDate today = LocalDate.now().minusDays(5);
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.plusDays(1).atStartOfDay();

        String sql = "SELECT id, usercode, longitude, latitude, create_time FROM user_location " +
                "WHERE usercode = ? AND create_time >= ? AND create_time < ? " +
                "ORDER BY create_time ASC";
        return jdbcTemplate.query(sql, new Object[]{usercode, startOfDay, endOfDay},
                new BeanPropertyRowMapper<>(UserLocation.class));
    }
     */
    public List<UserLocation> getTodayLocationsByUser(String usercode) {
        String sql = "SELECT id, usercode, longitude, latitude, create_time FROM user_location " +
                "WHERE usercode = ? " +
                "ORDER BY create_time ASC";

        return jdbcTemplate.query(sql,
                new Object[]{usercode},
                new BeanPropertyRowMapper<>(UserLocation.class));
    }
}