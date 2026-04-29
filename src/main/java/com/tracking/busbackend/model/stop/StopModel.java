package com.tracking.busbackend.model.stop;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StopModel {

    private String name;

    private double lat;

    private double lng;

    private int sequenceNo;
}
