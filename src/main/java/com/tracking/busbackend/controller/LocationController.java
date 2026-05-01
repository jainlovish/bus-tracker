package com.tracking.busbackend.controller;

import com.tracking.busbackend.entity.Driver;
import com.tracking.busbackend.entity.School;
import com.tracking.busbackend.model.location.LocationRequest;
import com.tracking.busbackend.entity.Route;
import com.tracking.busbackend.model.location.LocationResponse;
import com.tracking.busbackend.repository.DriverRepository;
import com.tracking.busbackend.repository.RouteRepo;
import com.tracking.busbackend.repository.SchoolRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class LocationController {

    private Map<String, LocationRequest> busLocationMap = new ConcurrentHashMap<>();

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private RouteRepo routeRepo;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private SchoolRepo schoolRepo;

    @PostMapping("/location")
    public ResponseEntity<?> updateLocation(@RequestBody LocationRequest request) {

        Driver driver = driverRepository.findById(request.getDriverId())
                .orElseThrow(() -> new RuntimeException("Invalid driver"));

        Route route = routeRepo.findById(driver.getRouteId())
                .orElseThrow(() -> new RuntimeException("Route not found"));

        String busId = route.getBusId();

        busLocationMap.put(busId, request);

        messagingTemplate.convertAndSend(
                "/topic/location/" + busId,
                request
        );

        return ResponseEntity.ok("Location updated");
    }

    @GetMapping("/location/{busId}")
    public ResponseEntity<LocationRequest> getLocation(@PathVariable String busId) {
        return ResponseEntity.ok(busLocationMap.get(busId));
    }

    @GetMapping("location-by-school")
    public ResponseEntity<List<LocationResponse>> getLocationBySchool(@RequestParam Long schoolId) {

        School school = schoolRepo.findById(schoolId).orElseThrow(() -> new RuntimeException("Invalid SchoolId"));

        List<String> busIdList = school.getRoutes().stream().map(s -> s.getBusId()).toList();

        List<LocationResponse> responses = busIdList.stream()
                .map(busId -> {
                    LocationRequest req = busLocationMap.get(busId);
                    if (req == null) return null;

                    LocationResponse res = new LocationResponse();
                    res.setBusId(busId);
                    res.setDriverId(req.getDriverId());
                    res.setLat(req.getLat());
                    res.setLng(req.getLng());
                    res.setTimestamp(req.getTimestamp());

                    return res;
                })
                .filter(Objects::nonNull)
                .toList();

        return ResponseEntity.ok(responses);
    }

}