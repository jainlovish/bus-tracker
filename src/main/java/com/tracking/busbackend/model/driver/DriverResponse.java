package com.tracking.busbackend.model.driver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverResponse {

    private Long driverId;

    private String name;

    private String mobile;

    private String email;

    private String password;

    private Boolean isActive;

    private Long routeId;

    private Long schoolId;
}
