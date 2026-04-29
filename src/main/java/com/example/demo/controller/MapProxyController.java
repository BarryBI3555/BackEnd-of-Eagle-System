package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api/map")
@CrossOrigin(origins = "*")
public class MapProxyController {

    @Value("${tencent.map.js-key}")
    private String jsKey;

    @Value("${tencent.map.domain}")
    private String mapDomain;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 获取地图JS API脚本
     */
    @GetMapping("/jsapi")
    public ResponseEntity<String> getMapJsApi() {
        try {
            // 构建腾讯地图API URL
            URI uri = UriComponentsBuilder.fromHttpUrl(mapDomain)
                    .path("/api/gljs")
                    .queryParam("v", "1.exp")
                    .queryParam("key", jsKey)
                    .queryParam("libraries", "visualization")
                    .build()
                    .toUri();

            // 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");
            HttpEntity<String> entity = new HttpEntity<>(headers);

            // 发起请求
            ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
            
           
            String responseBody = response.getBody();
            
            // 替换脚本中的腾讯地图域名为本地代理地址
            // if (responseBody != null) {
            //     responseBody = responseBody
            //         // API 服务域名
            //         // .replace("apis.map.qq.com", "localhost:8080/api/map/proxy/apis.map.qq.com")
            //         // .replace("map.qq.com", "localhost:8080/api/map/proxy/map.qq.com")
            //         // .replace("mapapi.qq.com", "localhost:8080/api/map/proxy/mapapi.qq.com")
            //         .replace("overseactrl.qq.com", "localhost:8080/api/map/proxy/overseactrl.qq.com")
            //         .replace("s.map.qq.com", "localhost:8080/api/map/proxy/s.map.qq.com")
            //         .replace("sv.map.qq.com", "localhost:8080/api/map/proxy/sv.map.qq.com")
            //         .replace("lbs.qq.com", "localhost:8080/api/map/proxy/lbs.qq.com")
            //         .replace("shp.qq.com", "localhost:8080/api/map/proxy/shp.qq.com")
            //         // 地图瓦片域名
            //         // .replace("rt0.map.gtimg.com", "localhost:8080/api/map/tiles/rt0.map.gtimg.com")
            //         // .replace("rt1.map.gtimg.com", "localhost:8080/api/map/tiles/rt1.map.gtimg.com")
            //         // .replace("rt2.map.gtimg.com", "localhost:8080/api/map/tiles/rt2.map.gtimg.com")
            //         // .replace("rt3.map.gtimg.com", "localhost:8080/api/map/tiles/rt3.map.gtimg.com")
            //         .replace("p0.map.gtimg.com", "localhost:8080/api/map/tiles/p0.map.gtimg.com")
            //         .replace("p1.map.gtimg.com", "localhost:8080/api/map/tiles/p1.map.gtimg.com")
            //         .replace("p2.map.gtimg.com", "localhost:8080/api/map/tiles/p2.map.gtimg.com")
            //         .replace("p3.map.gtimg.com", "localhost:8080/api/map/tiles/p3.map.gtimg.com")
            //         .replace("p4.map.gtimg.com", "localhost:8080/api/map/tiles/p4.map.gtimg.com")
            //         .replace("p5.map.gtimg.com", "localhost:8080/api/map/tiles/p5.map.gtimg.com")
            //         .replace("p6.map.gtimg.com", "localhost:8080/api/map/tiles/p6.map.gtimg.com")
            //         .replace("p7.map.gtimg.com", "localhost:8080/api/map/tiles/p7.map.gtimg.com")
            //         // 图片服务域名
            //         .replace("pr.map.qq.com", "localhost:8080/api/map/images/pr.map.qq.com")
            //         .replace("3gimg.qq.com", "localhost:8080/api/map/images/3gimg.qq.com")
            //         .replace("4gimg.qq.com", "localhost:8080/api/map/images/4gimg.qq.com")
            //         // 3G/4G 地图服务
            //         .replace("3g.map.qq.com", "localhost:8080/api/map/proxy/3g.map.qq.com")
            //         .replace("4g.map.qq.com", "localhost:8080/api/map/proxy/4g.map.qq.com");
            // }
            
            // 设置响应头
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.setContentType(MediaType.parseMediaType("application/javascript; charset=UTF-8"));
            responseHeaders.setCacheControl("public, max-age=3600"); // 缓存1小时
            
            return new ResponseEntity<>(responseBody, responseHeaders, HttpStatus.OK);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("// 地图API加载失败: " + e.getMessage());
        }
    }

    /**
     * 获取地图初始化配置（包含密钥的加密版本）
     */
    // @GetMapping("/config")
    // public ResponseEntity<Map<String, Object>> getMapConfig() {
    //     // 设置响应头，确保JSON使用UTF-8编码
    //     HttpHeaders headers = new HttpHeaders();
    //     headers.setContentType(MediaType.APPLICATION_JSON);
    //     headers.set("Content-Type", "application/json; charset=UTF-8");
        
    //     // 使用HashMap创建响应对象
    //     Map<String, Object> response = new java.util.HashMap<>();
    //     response.put("jsApiUrl", "api/map/jsapi");
    //     response.put("jsKeyHash", generateApiKeyHash(jsKey));
    //     System.out.println(response);
    //     response.put("timestamp", System.currentTimeMillis());
        
    //     return ResponseEntity.ok()
    //             .headers(headers)
    //             .body(response);
    // }

    // /**
    //  * 生成API密钥的哈希值（用于前端验证）
    //  */
    // private String generateApiKeyHash(String apiKey) {
    //     // 简单的哈希处理，实际项目中可以使用更复杂的加密
    //     return Integer.toHexString(apiKey.hashCode());
    // }
}
