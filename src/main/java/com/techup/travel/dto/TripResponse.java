package com.techup.travel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

/**
 * DTO สำหรับตอบกลับข้อมูลทริป
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TripResponse {

    private Long id;
    private String title;
    private String description;
    private List<String> photos;
    private List<String> tags;
    private Double latitude;
    private Double longitude;
    private Long authorId;
    private String authorDisplayName;
    private Instant createdAt;
    private Instant updatedAt;
}