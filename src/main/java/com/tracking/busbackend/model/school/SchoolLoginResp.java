package com.tracking.busbackend.model.school;

import com.tracking.busbackend.entity.Route;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SchoolLoginResp {

    private Long schoolId;

    private String name;

    private List<Route> routes;
}
