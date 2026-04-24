package com.tracking.busbackend.controller;

import com.tracking.busbackend.entity.Driver;
import com.tracking.busbackend.model.LocationRequest;
import com.tracking.busbackend.entity.Route;
import com.tracking.busbackend.entity.Stop;
import com.tracking.busbackend.repository.DriverRepository;
import com.tracking.busbackend.repository.RouteRepo;
import com.tracking.busbackend.repository.StopRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
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

    // ✅ DRIVER SENDS LOCATION
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

    // ✅ GET LAST LOCATION (for initial load)
    @GetMapping("/location/{busId}")
    public ResponseEntity<LocationRequest> getLocation(@PathVariable String busId) {
        return ResponseEntity.ok(busLocationMap.get(busId));
    }
}