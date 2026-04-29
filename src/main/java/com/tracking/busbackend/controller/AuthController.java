package com.tracking.busbackend.controller;

import com.tracking.busbackend.entity.Driver;
import com.tracking.busbackend.entity.Parent;
import com.tracking.busbackend.entity.School;
import com.tracking.busbackend.model.driver.DriverLoginResp;
import com.tracking.busbackend.model.login.LoginRequest;
import com.tracking.busbackend.model.parent.ParentLoginResp;
import com.tracking.busbackend.model.school.SchoolLoginResp;
import com.tracking.busbackend.repository.DriverRepository;
import com.tracking.busbackend.repository.ParentRepository;
import com.tracking.busbackend.repository.SchoolRepo;
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

    @Autowired
    private ParentRepository parentRepository;

    @Autowired
    private SchoolRepo schoolRepo;

    @PostMapping("/auth/driver")
    public ResponseEntity<?> driverLogin(@RequestBody LoginRequest request) {

        Driver driver = driverRepository
                .findByMobileOrEmail(request.getUsername(), request.getUsername())
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        if (!passwordEncoder.matches(request.getPassword(), driver.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
        }

        if (!driver.getIsActive()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User not active");
        }

        return ResponseEntity.ok(new DriverLoginResp(
                driver.getId(),
                driver.getName(),
                driver.getRouteId()
        ));
    }

    @PostMapping("/auth/parent")
    public ResponseEntity<?> parentLogin(@RequestBody LoginRequest request) {

        Parent parent = parentRepository
                .findByEmailOrMobile(request.getUsername(), request.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid Username"));

        if (!passwordEncoder.matches(request.getPassword(), parent.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
        }

        if (!parent.getIsActive()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User not active");
        }

            return ResponseEntity.ok(new ParentLoginResp(
                    parent.getId(),
                    parent.getName(),
                    parent.getSchool().getId()
            ));

    }

    @PostMapping("/auth/school")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        School school = schoolRepo
                .findByMobileOrEmail(request.getUsername(), request.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid Username"));

        if (!passwordEncoder.matches(request.getPassword(), school.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
        }

        if (!school.getIsActive()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User not active");
        }

        return ResponseEntity.ok(new SchoolLoginResp(school.getId(),
                school.getName(),
                school.getRoutes()));
    }

}
