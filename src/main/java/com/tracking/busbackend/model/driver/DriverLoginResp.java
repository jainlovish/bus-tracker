package com.tracking.busbackend.model.driver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DriverLoginResp {
    private Long driverId;
    private String name;
    private Long routeId;
}
