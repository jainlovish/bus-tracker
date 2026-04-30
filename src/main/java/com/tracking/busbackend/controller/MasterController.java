package com.tracking.busbackend.controller;

import com.tracking.busbackend.entity.*;
import com.tracking.busbackend.model.route.RouteResponse;
import com.tracking.busbackend.model.stop.StopModel;
import com.tracking.busbackend.repository.RouteRepo;
import com.tracking.busbackend.repository.SchoolRepo;
import com.tracking.busbackend.repository.StopRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    public ResponseEntity<List<School>> getSchools() {
        return ResponseEntity.ok(schoolRepo.findAll());
    }

    @GetMapping("/routes-by-school")
    public ResponseEntity<List<RouteResponse>> getRoutesBySchool(@RequestParam Long schoolId) {

        schoolRepo.findById(schoolId).orElseThrow(() -> new RuntimeException("Invalid SchoolId"));

        List<RouteResponse> routeResponse = mapRouteResponse(routeRepo.findBySchoolId(schoolId));

        return ResponseEntity.ok(routeResponse);
    }

    @GetMapping("/route/{id}")
    public ResponseEntity<Route> getRouteById(@PathVariable Long id){

        Route route = routeRepo.findById(id).orElseThrow(() -> new RuntimeException("Invalid RouteId"));

        return ResponseEntity.ok(route);
    }

    @GetMapping("/stops-by-bus")
    public ResponseEntity<List<Stop>> getStopsByBus(@RequestParam String busId) {

        Route route = routeRepo.findByBusId(busId);

        if (route == null) {
            return ResponseEntity.badRequest().build();
        }

        List<Stop> stops = stopRepo.findByRouteIdOrderBySequenceNo(route.getId());

        return ResponseEntity.ok(stops);
    }

    @GetMapping("/stops-by-route")
    public ResponseEntity<List<Stop>> getStopsByRoute(@RequestParam Long routeId) {

        routeRepo.findById(routeId).orElseThrow(() -> new RuntimeException("Invalid RouteId"));

        return ResponseEntity.ok(stopRepo.findByRouteIdOrderBySequenceNo(routeId));
    }

    @GetMapping("/drivers-by-school")
    public ResponseEntity<List<Driver>> getDriversBySchool(@RequestParam Long schoolId){

        School school = schoolRepo.findById(schoolId).orElseThrow(() -> new RuntimeException("Invalid SchoolId"));

        return ResponseEntity.ok(school.getDrivers());
    }

    @GetMapping("/parents-by-school")
    public ResponseEntity<List<Parent>> getParentsBySchool(@RequestParam Long schoolId){

        School school = schoolRepo.findById(schoolId).orElseThrow(() -> new RuntimeException("Invalid SchoolId"));

        return ResponseEntity.ok(school.getParents());
    }

    private List<RouteResponse> mapRouteResponse(List<Route> routeList){

        List<RouteResponse> routeResponses = new ArrayList<>();
        routeList.forEach(r -> {
              RouteResponse routeResponse = new RouteResponse();
              routeResponse.setRouteId(r.getId());
              routeResponse.setName(r.getName());
              routeResponse.setPolyline(r.getPolyline());
              routeResponse.setBusId(r.getBusId());
              routeResponse.setSchoolId(r.getSchool().getId());
              routeResponse.setStops(convertToStopModel(r.getStops()));

              routeResponses.add(routeResponse);
        });
        return routeResponses;
    }

    private List<StopModel> convertToStopModel(List<Stop> stopList){
        List<StopModel> stopModelList = new ArrayList<>();
        stopList.forEach(s -> {
            StopModel stopModel = new StopModel(s.getName(), s.getLat(), s.getLng(), s.getSequenceNo());
            stopModelList.add(stopModel);
        });
        return stopModelList;
    }

}