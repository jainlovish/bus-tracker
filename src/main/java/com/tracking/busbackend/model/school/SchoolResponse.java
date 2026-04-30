package com.tracking.busbackend.model.school;

import com.tracking.busbackend.model.route.RouteResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SchoolResponse {

    private Long schoolId;

    private String name;

    private String address;

    private String email;

    private String mobile;

    private String password;

    private Boolean isActive;

    private List<RouteResponse> routes;
}
