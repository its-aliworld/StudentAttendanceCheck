package com.attendance.controller;

import com.attendance.dto.LoginRequest;
import com.attendance.entity.User;
import com.attendance.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {
    private final UserRepository users;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AuthController(UserRepository users) {
        this.users = users;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        User user = users.findByEmail(request.getEmail()).orElse(null);

        if (user == null || !encoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body(Map.of("message", "Invalid email or password"));
        }

        return ResponseEntity.ok(Map.of(
                "id", user.getId(),
                "name", user.getName(),
                "email", user.getEmail(),
                "role", user.getRole().name()
        ));
    }
}
