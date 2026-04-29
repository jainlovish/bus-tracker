package com.tracking.busbackend.model.route;

import com.tracking.busbackend.entity.School;
import com.tracking.busbackend.entity.Stop;
import com.tracking.busbackend.model.stop.StopModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateRouteReq {

    private String name;

    private String busId;

    private String polyline;

    private Long schoolId;

    private List<StopModel> stops;
}
