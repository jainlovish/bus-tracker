package com.tracking.busbackend.model.driver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DriverRouteChangeReq {

    private Long routeId;
    private Long driverId;
}
