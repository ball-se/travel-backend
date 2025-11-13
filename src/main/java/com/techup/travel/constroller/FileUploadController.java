package com.techup.travel.constroller;

import com.techup.travel.service.SupabaseStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/files")  // เส้นนี้ต้องแนบ JWT ตาม SecurityConfig
@RequiredArgsConstructor
public class FileUploadController {

  private final SupabaseStorageService supabaseStorageService;

  @PostMapping("/Upload")
  public ResponseEntity<Map<String, String>> Upload(@RequestParam("file") MultipartFile file) {
    String url = supabaseStorageService.uploadFile(file);
    return ResponseEntity.ok(Map.of("url", url));
  }
}