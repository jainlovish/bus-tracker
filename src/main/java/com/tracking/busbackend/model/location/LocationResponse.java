package com.tracking.busbackend.model.location;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationResponse {

    private String busId;
    private long driverId;
    private double lat;
    private double lng;
    private String timestamp;
}
