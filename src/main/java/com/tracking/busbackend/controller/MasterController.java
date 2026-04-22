package com.tracking.busbackend.controller;

import com.tracking.busbackend.entity.Route;
import com.tracking.busbackend.entity.School;
import com.tracking.busbackend.entity.Stop;
import com.tracking.busbackend.repository.RouteRepo;
import com.tracking.busbackend.repository.SchoolRepo;
import com.tracking.busbackend.repository.StopRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class MasterController {

    @Autowired
    private SchoolRepo schoolRepo;

    @Autowired private RouteRepo routeRepo;
    @Autowired private StopRepo stopRepo;

    @GetMapping("/schools")
    public List<School> getSchools() {
        return schoolRepo.findAll();
    }

    @GetMapping("/routes")
    public List<Route> getRoutes(@RequestParam Long schoolId) {
        return routeRepo.findBySchoolId(schoolId);
    }

}