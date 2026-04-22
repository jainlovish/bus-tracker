package com.tracking.busbackend.controller;

import com.tracking.busbackend.model.LocationRequest;
import com.tracking.busbackend.entity.Route;
import com.tracking.busbackend.entity.Stop;
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
    private StopRepo stopRepo;

    // ✅ DRIVER SENDS LOCATION
    @PostMapping("/location")
    public ResponseEntity<?> updateLocation(@RequestBody LocationRequest request) {

        if (request.getBusId() == null || request.getBusId().isEmpty()) {
            return ResponseEntity.badRequest().body("Bus ID is required");
        }

        System.out.println("Bus: " + request.getBusId()
                + " Lat: " + request.getLat()
                + " Lng: " + request.getLng());

        busLocationMap.put(request.getBusId(), request);

        messagingTemplate.convertAndSend(
                "/topic/location/" + request.getBusId(),
                request
        );

        return ResponseEntity.ok("Location updated");
    }

    // ✅ GET LAST LOCATION (for initial load)
    @GetMapping("/location/{busId}")
    public ResponseEntity<LocationRequest> getLocation(@PathVariable String busId) {
        return ResponseEntity.ok(busLocationMap.get(busId));
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