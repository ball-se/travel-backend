package com.techup.travel.service;

import com.techup.travel.entity.User;
import com.techup.travel.repository.UserRepository;
import com.techup.travel.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final JwtService jwtService;
  private final BCryptPasswordEncoder passwordEncoder;

  // ✅ สมัครสมาชิกใหม่
  public String register(String email, String password, String displayName) {
    if (userRepository.findByEmail(email).isPresent()) {
      throw new RuntimeException("Email already exists");
    }

    User user = User.builder()
        .email(email)
        .password(passwordEncoder.encode(password))
        .displayName(displayName)
        .build();

    userRepository.save(user);
    return "Registered successfully";
  }

  // ✅ เข้าสู่ระบบและสร้าง token
  public String login(String email, String password) {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("User not found"));

    if (!passwordEncoder.matches(password, user.getPassword())) {
      throw new RuntimeException("Invalid credentials");
    }

    return jwtService.generateToken(user.getEmail());
  }

  public User findByEmailOrThrow(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("User not found"));
  }
}
