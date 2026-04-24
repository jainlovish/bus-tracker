package com.tracking.busbackend.controller;

import com.tracking.busbackend.entity.Route;
import com.tracking.busbackend.entity.School;
import com.tracking.busbackend.entity.Stop;
import com.tracking.busbackend.repository.RouteRepo;
import com.tracking.busbackend.repository.SchoolRepo;
import com.tracking.busbackend.repository.StopRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class MasterController {

    @Autowired
    private SchoolRepo schoolRepo;

    @Autowired
    private RouteRepo routeRepo;

    @Autowired
    private StopRepo stopRepo;

    @GetMapping("/schools")
    public List<School> getSchools() {
        return schoolRepo.findAll();
    }

    @GetMapping("/routes")
    public List<Route> getRoutes(@RequestParam Long schoolId) {
        return routeRepo.findBySchoolId(schoolId);
    }

    @GetMapping("/routes/{id}")
    public Route getRouteById(@PathVariable Long id){
        return routeRepo.findById(id).get();
    }

    // ✅ GET STOPS USING BUS ID (MAIN FIX)
    @GetMapping("/stops/by-bus/{busId}")
    public ResponseEntity<List<Stop>> getStopsByBus(@PathVariable String busId) {

        Route route = routeRepo.findByBusId(busId);

        if (route == null) {
            return ResponseEntity.badRequest().build();
        }

        List<Stop> stops = stopRepo.findByRouteIdOrderBySequenceNo(route.getId());

        return ResponseEntity.ok(stops);
    }

    // ✅ FOR HOMEPAGE DROPDOWN
    @GetMapping("/stops/by-route")
    public List<Stop> getStopsByRoute(@RequestParam Long routeId) {
        return stopRepo.findByRouteIdOrderBySequenceNo(routeId);
    }

    @GetMapping("/routes/all")
    public List<Route> getAllRoutes() {
        return routeRepo.findAll();
    }

}