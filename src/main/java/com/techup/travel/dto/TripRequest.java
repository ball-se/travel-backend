package com.techup.travel.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.List;

/**
 * DTO รับข้อมูลเวลาสร้าง/แก้ไขทริป
 * - ใส่ Validation เพื่อป้องกัน input ไม่ครบหรือเกินขนาดที่กำหนด
 */
@Data
public class TripRequest {

    @NotBlank(message = "title ห้ามว่าง")
    @Size(max = 1_000, message = "title ต้องไม่เกิน 1,000 ตัวอักษร")
    private String title;

    @Size(max = 1_000, message = "description ต้องไม่เกิน 1,000 ตัวอักษร")
    private String description;

    @NotBlank(message = "photos ห้ามว่าง")
    @Size(max = 10_000, message = "photos ต้องไม่เกิน 10,000 ตัวอักษร")
    private List<String> photos;

    @NotBlank(message = "tags ห้ามว่าง")
    @Size(max = 1_000, message = "tags ต้องไม่เกิน 1,000 ตัวอักษร")
    private List<String> tags;

    @NotNull(message = "latitude ห้ามว่าง")
    @DecimalMin(value = "-90.0", message = "latitude ต้องไม่น้อยกว่า -90")
    @DecimalMax(value = "90.0", message = "latitude ต้องไม่มากกว่า 90")
    private Double latitude;

    @NotNull(message = "longitude ห้ามว่าง")
    @DecimalMin(value = "-180.0", message = "longitude ต้องไม่น้อยกว่า -180")
    @DecimalMax(value = "180.0", message = "longitude ต้องไม่มากกว่า 180")
    private Double longitude;
}