package com.tracking.busbackend.controller;

import com.tracking.busbackend.entity.Parent;
import com.tracking.busbackend.model.ParentLoginReq;
import com.tracking.busbackend.model.ParentLoginResp;
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
public class ParentController {

    @Autowired
    private ParentRepository parentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/parent/login")
    public ResponseEntity<?> login(@RequestBody ParentLoginReq request) {

        try {
            Parent parent = parentRepository
                    .findByEmailOrMobile(request.getUsername(), request.getUsername())
                    .orElseThrow(() -> new RuntimeException("Invalid user"));

            if (!parent.getIsActive()) {
                throw new RuntimeException("User not active");
            }

            if (!passwordEncoder.matches(request.getPassword(), parent.getPassword())) {
                throw new RuntimeException("Invalid password");
            }

            return ResponseEntity.ok(new ParentLoginResp(
                    parent.getId(),
                    parent.getName(),
                    parent.getSchoolId()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }
}