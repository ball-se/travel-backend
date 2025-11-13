package com.techup.travel.constroller;

import com.techup.travel.security.JwtService;
import com.techup.travel.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.techup.travel.entity.User;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;
  private final JwtService jwtService;

  // ✅ Register
  @PostMapping("/register")
  public String register(@RequestBody Map<String, String> req) {
    return authService.register(req.get("email"), req.get("password"), req.get("display_name"));
  }

  // ✅ Login
  @PostMapping("/login")
  public Map<String, String> login(@RequestBody Map<String, String> req) {
    String token = authService.login(req.get("email"), req.get("password"));
    return Map.of("token", token);
  }

  // ✅ /auth/me — แสดง email จาก token
  @GetMapping("/me")
  public Map<String, Object> me(@RequestHeader("Authorization") String header) {
    String token = header.replace("Bearer ", "");
    String email = jwtService.extractEmail(token);
  
    User user = authService.findByEmailOrThrow(email); // หรือ method อื่นที่คุณมีอยู่
  
    return Map.of(
        "id", user.getId(),
        "email", user.getEmail(),
        "displayName", user.getDisplayName(),
        "createdAt", user.getCreatedAt()
    );
  }
}
