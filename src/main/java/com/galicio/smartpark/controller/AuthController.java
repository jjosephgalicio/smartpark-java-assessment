package com.galicio.smartpark.controller;

import com.galicio.smartpark.dto.AuthRequest;
import com.galicio.smartpark.dto.AuthResponse;
import com.galicio.smartpark.util.JwtTokenUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtTokenUtil jwtTokenUtil;

    public AuthController(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        // Static authentication per instructions
        if ("galicio".equals(request.getUsername()) && "smartpark".equals(request.getPassword())) {
            String token = jwtTokenUtil.generateToken(request.getUsername());
            return ResponseEntity.ok(new AuthResponse(token));
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }
}