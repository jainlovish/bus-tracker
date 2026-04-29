package com.tracking.busbackend.controller;

import com.tracking.busbackend.entity.Admin;
import com.tracking.busbackend.entity.School;
import com.tracking.busbackend.model.login.LoginRequest;
import com.tracking.busbackend.model.login.LoginResponse;
import com.tracking.busbackend.model.school.ModifySchoolReq;
import com.tracking.busbackend.repository.AdminRepository;
import com.tracking.busbackend.repository.SchoolRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AdminController {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private SchoolRepo schoolRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/admin/login")
    public ResponseEntity<?> loginAdmin(@RequestBody LoginRequest loginRequest) {

        Admin admin = adminRepository.findByMobileOrEmail(loginRequest.getUsername(), loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("Incorrect Username"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), admin.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Password");
        }

        return ResponseEntity.ok(new LoginResponse("Welcome Admin !!"));
    }

    @PostMapping("/admin/add-school")
    public ResponseEntity<?> createSchool(@RequestBody School school) {

        school.setPassword(passwordEncoder.encode(school.getPassword()));

        return ResponseEntity.ok(schoolRepo.save(school));
    }

    @GetMapping("/admin/delete-school")
    public ResponseEntity<?> deleteSchool(@RequestParam Long schoolId) {

        schoolRepo.findById(schoolId).orElseThrow(() -> new RuntimeException("Invalid SchoolId"));

        schoolRepo.deleteById(schoolId);

        return ResponseEntity.ok("School Deleted Successfully");
    }

    @PutMapping("/admin/modify-school")
    public ResponseEntity<?> modifySchool(@RequestBody ModifySchoolReq modifySchoolReq) {

        School school = schoolRepo.findById(modifySchoolReq.getSchoolId()).orElseThrow(() -> new RuntimeException("Invalid SchoolId"));

        mapSchool(modifySchoolReq, school);

        return ResponseEntity.ok(schoolRepo.save(school));
    }

    private void mapSchool(ModifySchoolReq modifySchoolReq, School school){

        school.setName(modifySchoolReq.getName());
        school.setEmail(modifySchoolReq.getEmail());
        school.setPassword(passwordEncoder.encode(modifySchoolReq.getPassword()));
        school.setIsActive(modifySchoolReq.getIsActive());
        school.setAddress(modifySchoolReq.getAddress());
    }

}
