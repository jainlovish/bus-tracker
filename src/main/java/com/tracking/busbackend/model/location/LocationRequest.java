package com.tracking.busbackend.model.location;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LocationRequest {
    private long driverId;
    private double lat;
    private double lng;
    private String timestamp;
}
