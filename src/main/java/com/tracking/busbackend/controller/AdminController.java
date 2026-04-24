package com.tracking.busbackend.controller;

import com.tracking.busbackend.entity.Driver;
import com.tracking.busbackend.entity.Parent;
import com.tracking.busbackend.repository.DriverRepository;
import com.tracking.busbackend.repository.ParentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AdminController {

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private ParentRepository parentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/admin/driver")
    public ResponseEntity<?> createDriver(@RequestBody Driver driver) {

        driver.setPassword(passwordEncoder.encode(driver.getPassword()));

        return ResponseEntity.ok(driverRepository.save(driver));
    }

    @PostMapping("/admin/parent")
    public ResponseEntity<?> createParent(@RequestBody Parent parent) {

        parent.setPassword(passwordEncoder.encode(parent.getPassword()));

        return ResponseEntity.ok(parentRepository.save(parent));
    }
}
