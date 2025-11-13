package com.techup.travel.constroller;

import com.techup.travel.dto.TripRequest;
import com.techup.travel.dto.TripResponse;
import com.techup.travel.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;

import com.techup.travel.service.SupabaseStorageService;

/**
 * REST controller สำหรับอ่านข้อมูลทริป
 */
@RestController
@RequestMapping("/api/trips")
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;
    private final SupabaseStorageService supabaseStorageService; // ✅ ฉีด service อัปโหลดเข้ามา

    /** GET /api/trips/search - ค้นหาทริปตามคำค้นหา */
    @GetMapping("/search")
    public List<TripResponse> searchTrips(@RequestParam("query") String query) {
        if (query == null || query.isBlank()) {
            return List.of();
        }
        return tripService.search(query.trim());
    }

    /** GET /api/trips - ดึงทริปทั้งหมด */
    @GetMapping
    public List<TripResponse> getTrips() {
        return tripService.getAll();
    }

    // /** GET /api/trips/mine - ดึงทริปของผู้ใช้งาน */
    // @GetMapping("/mine")
    // public List<TripResponse> getMyTrips(@RequestHeader("Authorization") String header) {
        
    // }

    /** GET /api/trips/{id} - ดึงทริปรายการเดียว */
    @GetMapping("/{id}")
    public TripResponse getTrip(@PathVariable Long id) {
        return tripService.getById(id);
    }

    /** POST /api/trips - สร้างทริปใหม่ */
    @PostMapping
    public ResponseEntity<TripResponse> create(@RequestBody TripRequest request) {
        TripResponse created = tripService.create(request);
        URI location = URI.create("/api/trips/" + created.getId());
        return ResponseEntity.created(location).body(created);
    }

    @PostMapping("/{id}/upload")
    public ResponseEntity<TripResponse> uploadForTrip(@PathVariable Long id,
                                                        @RequestParam("file") MultipartFile file) {
        String url = supabaseStorageService.uploadFile(file);
        TripResponse updated = tripService.attachFileUrl(id, url);
        return ResponseEntity.ok(updated);
    }
}