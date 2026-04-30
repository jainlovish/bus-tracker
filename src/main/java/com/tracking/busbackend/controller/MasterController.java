package com.tracking.busbackend.controller;

import com.tracking.busbackend.entity.*;
import com.tracking.busbackend.model.route.RouteResponse;
import com.tracking.busbackend.model.school.SchoolResponse;
import com.tracking.busbackend.model.stop.StopModel;
import com.tracking.busbackend.repository.RouteRepo;
import com.tracking.busbackend.repository.SchoolRepo;
import com.tracking.busbackend.repository.StopRepo;
import com.tracking.busbackend.util.ConvertorUtils;
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
    public ResponseEntity<List<SchoolResponse>> getSchools() {

        List<SchoolResponse> schoolResponses = convertToSchoolResponse(schoolRepo.findAll());

        return ResponseEntity.ok(schoolResponses);
    }

    @GetMapping("/routes-by-school")
    public ResponseEntity<List<RouteResponse>> getRoutesBySchool(@RequestParam Long schoolId) {

        schoolRepo.findById(schoolId).orElseThrow(() -> new RuntimeException("Invalid SchoolId"));

        List<RouteResponse> routeResponse = ConvertorUtils.convertToRouteResponse(routeRepo.findBySchoolId(schoolId));

        return ResponseEntity.ok(routeResponse);
    }

    @GetMapping("/route/{id}")
    public ResponseEntity<RouteResponse> getRouteById(@PathVariable Long id){

        Route route = routeRepo.findById(id).orElseThrow(() -> new RuntimeException("Invalid RouteId"));

        RouteResponse routeResponse = new RouteResponse(route.getId(), route.getName(), route.getBusId(), route.getPolyline(), route.getSchool().getId(), ConvertorUtils.convertToStopModel(route.getStops()));

        return ResponseEntity.ok(routeResponse);
    }

    @GetMapping("/stops-by-bus")
    public ResponseEntity<List<StopModel>> getStopsByBus(@RequestParam String busId) {

        Route route = routeRepo.findByBusId(busId);

        if (route == null) {
            return ResponseEntity.badRequest().build();
        }

        List<Stop> stops = stopRepo.findByRouteIdOrderBySequenceNo(route.getId());

        List<StopModel> stopModelList = ConvertorUtils.convertToStopModel(stops);

        return ResponseEntity.ok(stopModelList);
    }

    @GetMapping("/stops-by-route")
    public ResponseEntity<List<StopModel>> getStopsByRoute(@RequestParam Long routeId) {

        routeRepo.findById(routeId).orElseThrow(() -> new RuntimeException("Invalid RouteId"));

        List<Stop> stopsList = stopRepo.findByRouteIdOrderBySequenceNo(routeId);

        List<StopModel> stopModelList = ConvertorUtils.convertToStopModel(stopsList);

        return ResponseEntity.ok(stopModelList);
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

    private List<SchoolResponse> convertToSchoolResponse(List<School> schoolList){
        List<SchoolResponse> schoolResponses = new ArrayList<>();

        schoolList.forEach(s -> {
            SchoolResponse schoolResponse = new SchoolResponse(s.getId(), s.getName(), s.getAddress(), s.getEmail(), s.getMobile(), s.getPassword(), s.getIsActive(), ConvertorUtils.convertToRouteResponse(s.getRoutes()));
            schoolResponses.add(schoolResponse);
        });

        return schoolResponses;
    }

}