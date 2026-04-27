package com.example.demo.util;

import com.example.demo.entity.LocationCache;
import com.example.demo.entity.UserLocation;
import com.example.demo.mapper.LocationCacheMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class LocationAddressConverter {

    private static final Logger logger = LoggerFactory.getLogger(LocationAddressConverter.class);

    // 读取你配置的腾讯KEY
    @Value("${tencent.map.key}")
    private String tencentMapKey;
    @Value("${tencent.map.api-domain}")
    private String tencentMapDomain;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final LocationCacheMapper locationCacheMapper;

    // 线程池 - 单线程顺序执行，确保严格的速率控制
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    // 请求计数器和上次重置时间
    private final AtomicInteger requestCount = new AtomicInteger(0);
    private volatile long lastResetTime = System.currentTimeMillis();
    
    // 每秒最大请求数（保守设置为3，腾讯地图免费版通常是5，但建议留有余量）
    private static final int MAX_REQUESTS_PER_SECOND = 3;
    // 请求间隔（毫秒）- 确保每个请求之间有足够的间隔
    private static final long REQUEST_INTERVAL_MS = 350; // 350ms = 每秒约2.8个请求

    public LocationAddressConverter(LocationCacheMapper locationCacheMapper) {
        this.locationCacheMapper = locationCacheMapper;
    }

    public List<UserLocation> convertBatch(List<UserLocation> locationList) {
        if (locationList == null || locationList.isEmpty()) return locationList;

        // 过滤出需要解析的坐标
        List<UserLocation> needConvertList = new ArrayList<>();
        for (UserLocation loc : locationList) {

            if (loc.getLongitude() == null || loc.getLatitude() == null ||
                Double.isNaN(loc.getLongitude()) || Double.isNaN(loc.getLatitude())) {
                loc.setAddress("坐标无效");
                continue;
            }

            // 验证坐标范围（中国地区大致范围）
            if (loc.getLongitude() < 73 || loc.getLongitude() > 135 ||
                loc.getLatitude() < 3 || loc.getLatitude() > 53) {
                loc.setAddress("坐标范围无效");
                continue;
            }

            // 先查询数据库缓存（只查询有效的缓存）
            LocationCache cache = locationCacheMapper.findValidByCoordinates(
                formatDouble(loc.getLongitude(), 3), 
                formatDouble(loc.getLatitude(), 3), 
                formatDate(LocalDateTime.now())
            );
            if (cache != null) {
                loc.setAddress(cache.getAddress());
                continue;
            }

            needConvertList.add(loc);
        }

        // 顺序处理需要转换的坐标（单线程，严格控制速率）
        if (!needConvertList.isEmpty()) {
            // 对需要处理的坐标去重（根据经纬度）
            List<UserLocation> uniqueList = new ArrayList<>();
            Set<String> processedCoords = new HashSet<>();
            Map<String, UserLocation> coordToLocation = new HashMap<>();

            for (UserLocation loc : needConvertList) {
                String coordKey = formatDouble(loc.getLongitude(), 3) + "," + formatDouble(loc.getLatitude(), 3);
                if (!processedCoords.contains(coordKey)) {
                    processedCoords.add(coordKey);
                    coordToLocation.put(coordKey, loc);
                }
            }
            uniqueList.addAll(coordToLocation.values());

            logger.info("开始顺序处理 {} 个唯一坐标", uniqueList.size());

            // 顺序处理每个坐标，严格控制请求速率
            for (UserLocation loc : uniqueList) {
                try {
                    // 等待直到可以发送请求
                    waitForRateLimit();
                    
                    // 执行转换
                    convertSingleLocation(loc);
                    
                    // 请求成功后增加计数
                    int count = requestCount.incrementAndGet();
                    logger.debug("已发送第 {} 个请求", count);
                    
                } catch (Exception e) {
                    logger.error("坐标解析异常: {}", e.getMessage());
                    loc.setAddress("解析异常");
                }
            }

            // 将解析结果回填到原列表中相同坐标的位置
            for (UserLocation loc : needConvertList) {
                String coordKey = formatDouble(loc.getLongitude(), 3) + "," + formatDouble(loc.getLatitude(), 3);
                UserLocation processedLoc = coordToLocation.get(coordKey);
                if (processedLoc != null) {
                    loc.setAddress(processedLoc.getAddress());
                }
            }
            
            logger.info("坐标处理完成");
        }

        return locationList;
    }

    /**
     * 等待直到可以发送请求（严格的速率控制）
     */
    private synchronized void waitForRateLimit() {
        long now = System.currentTimeMillis();
        long timeSinceLastReset = now - lastResetTime;
        
        // 如果已经过去1秒，重置计数器
        if (timeSinceLastReset >= 1000) {
            requestCount.set(0);
            lastResetTime = now;
            logger.debug("重置请求计数器");
        }
        
        // 如果已经达到每秒最大请求数，等待到下一秒
        if (requestCount.get() >= MAX_REQUESTS_PER_SECOND) {
            long waitTime = 1000 - timeSinceLastReset + 50; // 多等50ms确保安全
            logger.warn("达到速率限制，等待 {} ms", waitTime);
            try {
                Thread.sleep(waitTime);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            // 重置计数器
            requestCount.set(0);
            lastResetTime = System.currentTimeMillis();
        }
        
        // 确保每个请求之间有足够的间隔
        long timeSinceLastRequest = now - lastResetTime;
        if (requestCount.get() > 0 && timeSinceLastRequest < REQUEST_INTERVAL_MS * requestCount.get()) {
            long waitTime = REQUEST_INTERVAL_MS * requestCount.get() - timeSinceLastRequest;
            if (waitTime > 0) {
                logger.debug("请求间隔控制，等待 {} ms", waitTime);
                try {
                    Thread.sleep(waitTime);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private void convertSingleLocation(UserLocation loc) {
        int retryCount = 0;
        int maxRetries = 3;
        
        while (retryCount < maxRetries) {
            try {
                Double lng = formatDouble(loc.getLongitude(), 3);
                Double lat = formatDouble(loc.getLatitude(), 3);

                String url = tencentMapDomain + "/ws/geocoder/v1/"
                        + "?location=" + lat + "," + lng
                        + "&key=" + tencentMapKey;

                logger.debug("发送请求: lat={}, lng={}", lat, lng);
                String jsonResp = restTemplate.getForObject(url, String.class);
                JsonNode root = objectMapper.readTree(jsonResp);

                String address;
                int status = root.path("status").asInt();
                
                if (status == 0) {
                    address = root.path("result").path("address").asText();
                    // 存入或更新缓存
                    updateToCache(lng, lat, address);
                    loc.setAddress(address);
                    logger.debug("请求成功: {}", address);
                    return; // 成功，退出重试循环
                } else {
                    String message = root.path("message").asText();
                    // 检查是否是配额超限错误
                    if (message.contains("上限") || message.contains("配额") || status == 121) {
                        logger.error("API配额超限: {}", message);
                        loc.setAddress("API配额超限");
                        return; // 配额问题，不再重试
                    }
                    address = message;
                    loc.setAddress(address);
                    logger.warn("API返回错误: status={}, message={}", status, message);
                    return; // 其他错误，不再重试
                }
                
            } catch (Exception e) {
                retryCount++;
                logger.error("请求异常（重试 {}/{}）: {}", retryCount, maxRetries, e.getMessage());
                
                if (retryCount >= maxRetries) {
                    loc.setAddress("解析异常");
                } else {
                    // 指数退避等待
                    long waitTime = (long) Math.pow(2, retryCount) * 1000;
                    logger.info("等待 {} ms 后重试", waitTime);
                    try {
                        Thread.sleep(waitTime);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        loc.setAddress("请求被中断");
                        return;
                    }
                }
            }
        }
    }

    private void updateToCache(Double longitude, Double latitude, String address) {
        try {
            // 先查询是否存在缓存
            LocationCache existingCache = locationCacheMapper.findByCoordinates(longitude, latitude);
            if (existingCache != null) {
                // 更新缓存
                existingCache.setAddress(address);
                existingCache.setUpdateTime(formatDate(LocalDateTime.now()));
                existingCache.setExpireTime(formatDate(LocalDateTime.now().plusDays(180)));
                locationCacheMapper.updateById(existingCache);
            } else {
                // 插入新缓存
                saveToCache(longitude, latitude, address);
            }
        } catch (Exception e) {
            logger.error("缓存更新失败: {}", e.getMessage());
        }
    }

    private void saveToCache(Double longitude, Double latitude, String address) {
        try {
            LocationCache cache = new LocationCache();
            cache.setLongitude(longitude);
            cache.setLatitude(latitude);
            cache.setAddress(address);
            cache.setCreateTime(formatDate(LocalDateTime.now()));
            cache.setUpdateTime(formatDate(LocalDateTime.now()));
            cache.setExpireTime(formatDate(LocalDateTime.now().plusDays(180)));
            locationCacheMapper.insert(cache);
        } catch (Exception e) {
            logger.error("缓存插入失败: {}", e.getMessage());
        }
    }

    public String formatDate(LocalDateTime dateTime) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateFormatter.format(dateTime);
    }

    private double formatDouble(double value, int decimalPlaces) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(decimalPlaces, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}