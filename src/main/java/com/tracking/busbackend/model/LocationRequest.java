package com.tracking.busbackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LocationRequest {
    private String busId;
    private double lat;
    private double lng;
    private String timestamp;
}
