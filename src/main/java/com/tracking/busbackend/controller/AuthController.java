package com.tracking.busbackend.controller;

import com.tracking.busbackend.entity.Driver;
import com.tracking.busbackend.model.LoginRequest;
import com.tracking.busbackend.model.LoginResponse;
import com.tracking.busbackend.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        Driver driver = driverRepository
                .findByMobileOrEmail(request.getUsername(), request.getUsername())
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        if (!passwordEncoder.matches(request.getPassword(), driver.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
        }

        if (!driver.isActive()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Driver not active");
        }

        return ResponseEntity.ok(new LoginResponse(
                driver.getId(),
                driver.getName(),
                driver.getRouteId()
        ));
    }
}
