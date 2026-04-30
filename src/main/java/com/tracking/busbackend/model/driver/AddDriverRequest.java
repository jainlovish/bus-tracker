package com.tracking.busbackend.model.driver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddDriverRequest {

    private String name;

    private String mobile;

    private String email;

    private String password;

    private Boolean isActive;

    private Long routeId;

    private Long schoolId;
}
