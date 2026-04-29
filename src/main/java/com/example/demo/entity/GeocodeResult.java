package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeocodeResult {
    private Integer status;
    private String message;
    private String requestId;
    private Result result;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Result {
        private String title;
        private Location location;
        private AddressComponents addressComponents;
        private AdInfo adInfo;
        private Integer reliability;
        private Integer level;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Location {
        private Double lat;
        private Double lng;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddressComponents {
        private String province;
        private String city;
        private String district;
        private String street;
        private String streetNumber;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AdInfo {
        private String adcode;
    }
}
