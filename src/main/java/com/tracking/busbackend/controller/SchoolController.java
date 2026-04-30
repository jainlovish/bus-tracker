package com.tracking.busbackend.controller;

import com.tracking.busbackend.entity.*;
import com.tracking.busbackend.model.driver.AddDriverRequest;
import com.tracking.busbackend.model.driver.DriverRouteChangeReq;
import com.tracking.busbackend.model.parent.AddParentRequest;
import com.tracking.busbackend.model.route.CreateRouteReq;
import com.tracking.busbackend.model.route.ModifyRouteReq;
import com.tracking.busbackend.model.route.RouteResponse;
import com.tracking.busbackend.model.stop.StopModel;
import com.tracking.busbackend.repository.*;
import com.tracking.busbackend.util.ConvertorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SchoolController {

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private ParentRepository parentRepository;

    @Autowired
    private SchoolRepo schoolRepo;

    @Autowired
    private RouteRepo routeRepo;

    @Autowired
    private StopRepo stopRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/school/add-driver")
    public ResponseEntity<String> createDriver(@RequestBody AddDriverRequest addDriverRequest) {

        Driver driver = new Driver();

        driver.setName(addDriverRequest.getName());
        driver.setPassword(passwordEncoder.encode(addDriverRequest.getPassword()));
        driver.setEmail(addDriverRequest.getEmail());
        driver.setRouteId(addDriverRequest.getRouteId());
        driver.setMobile(addDriverRequest.getMobile());
        driver.setIsActive(true);

        School school = schoolRepo.findById(addDriverRequest.getSchoolId()).orElseThrow(() -> new RuntimeException("Invalid SchoolId"));

        driver.setSchool(school);

        driverRepository.save(driver);

        return ResponseEntity.ok("Driver saved successfully");
    }

    @PostMapping("/school/change-driver-route")
    public ResponseEntity<?> changeDriverRoute(@RequestBody DriverRouteChangeReq driverRouteChangeReq) {

        Driver driver = driverRepository.findById(driverRouteChangeReq.getDriverId()).orElseThrow(() -> new RuntimeException("Invalid DriverId"));

        routeRepo.findById(driverRouteChangeReq.getRouteId()).orElseThrow(() -> new RuntimeException("Invalid RouteId"));

        if(driver.getRouteId().equals(driverRouteChangeReq.getRouteId())){
            throw new RuntimeException("Driver already on same route");
        }

        driver.setRouteId(driverRouteChangeReq.getRouteId());
        driverRepository.save(driver);

        return ResponseEntity.ok("Driver updated with Route Successfully");
    }

    @GetMapping("/school/delete-driver")
    public ResponseEntity<?> deleteDriver(@RequestParam Long driverId) {

        driverRepository.findById(driverId).orElseThrow(() -> new RuntimeException("Invalid DriverId"));

        driverRepository.deleteById(driverId);

        return ResponseEntity.ok("Driver Deleted Successfully");
    }

    @PostMapping("/school/add-parent")
    public ResponseEntity<String> createParent(@RequestBody AddParentRequest addParentRequest) {

        School school = schoolRepo.findById(addParentRequest.getSchoolId()).orElseThrow(() -> new RuntimeException("Invalid SchoolId"));

        Parent parent = new Parent();
        parent.setName(addParentRequest.getName());
        parent.setEmail(addParentRequest.getEmail());
        parent.setMobile(addParentRequest.getMobile());
        parent.setPassword(passwordEncoder.encode(addParentRequest.getPassword()));
        parent.setIsActive(true);
        parent.setSchool(school);

        parentRepository.save(parent);

        return ResponseEntity.ok("Parent saved successfully");
    }

    @GetMapping("/school/delete-parent")
    public ResponseEntity<String> deleteParent(@RequestParam Long parentId) {

        parentRepository.findById(parentId).orElseThrow(() -> new RuntimeException("Invalid ParentId"));

        parentRepository.deleteById(parentId);

        return ResponseEntity.ok("Parent Deleted Successfully");
    }

    @PostMapping("/school/add-route")
    public ResponseEntity<RouteResponse> createRoute(@RequestBody CreateRouteReq createRouteReq) {

        School school = schoolRepo.findById(createRouteReq.getSchoolId()).orElseThrow(() -> new RuntimeException("Invalid SchoolId"));

        Route route = new Route();
        route.setPolyline(createRouteReq.getPolyline());
        route.setName(createRouteReq.getName());
        route.setBusId(createRouteReq.getBusId());
        route.setSchool(school);

        routeRepo.save(route);

        mapStops(route, createRouteReq.getStops());

        route = routeRepo.save(route);

        RouteResponse routeResponse = new RouteResponse(route.getId(), route.getName(), route.getBusId(), route.getPolyline(), route.getSchool().getId(), ConvertorUtils.convertToStopModel(route.getStops()));

        return ResponseEntity.ok(routeResponse);
    }

    @PutMapping("/school/modify-route")
    public ResponseEntity<RouteResponse> modifyRoute(@RequestBody ModifyRouteReq modifyRouteReq) {

        Route route = routeRepo.findById(modifyRouteReq.getRouteId()).orElseThrow(() -> new RuntimeException("Invalid RouteId"));

        mapStops(route, modifyRouteReq.getStops());

        route.setName(modifyRouteReq.getName());
        route.setBusId(modifyRouteReq.getBusId());
        route.setPolyline(modifyRouteReq.getPolyline());

        RouteResponse routeResponse = new RouteResponse(route.getId(), route.getName(), route.getBusId(), route.getPolyline(), route.getSchool().getId(), ConvertorUtils.convertToStopModel(route.getStops()));

        return ResponseEntity.ok(routeResponse);
    }

    @GetMapping("/school/delete-route")
    public ResponseEntity<String> deleteRoute(@RequestParam Long routeId) {

        routeRepo.findById(routeId).orElseThrow(() -> new RuntimeException("Invalid RouteId"));

        routeRepo.deleteById(routeId);

        return ResponseEntity.ok("Route Deleted Successfully");
    }

    private void mapStops(Route route, List<StopModel> stopModelList){

        List<Stop> stopsList = route.getStops();

        if(!CollectionUtils.isEmpty(stopsList)) {
            stopsList.forEach(stop -> stopRepo.deleteById(stop.getId()));
        }

        stopModelList.stream().forEach(s -> {
            Stop stop = new Stop();
            stop.setName(s.getName());
            stop.setLat(s.getLat());
            stop.setLng(s.getLng());
            stop.setSequenceNo(s.getSequenceNo());
            stop.setRoute(route);

            stopRepo.save(stop);
        });
    }
}
