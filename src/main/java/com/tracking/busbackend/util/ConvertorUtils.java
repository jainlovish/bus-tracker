package com.tracking.busbackend.util;

import com.tracking.busbackend.entity.Route;
import com.tracking.busbackend.entity.Stop;
import com.tracking.busbackend.model.route.RouteResponse;
import com.tracking.busbackend.model.stop.StopModel;

import java.util.ArrayList;
import java.util.List;

public class ConvertorUtils {

    public static List<StopModel> convertToStopModel(List<Stop> stopList){
        List<StopModel> stopModelList = new ArrayList<>();
        stopList.forEach(s -> {
            StopModel stopModel = new StopModel(s.getName(), s.getLat(), s.getLng(), s.getSequenceNo());
            stopModelList.add(stopModel);
        });
        return stopModelList;
    }

    public static List<RouteResponse> convertToRouteResponse(List<Route> routeList){

        List<RouteResponse> routeResponses = new ArrayList<>();
        routeList.forEach(r -> {
            RouteResponse routeResponse = new RouteResponse();
            routeResponse.setRouteId(r.getId());
            routeResponse.setName(r.getName());
            routeResponse.setPolyline(r.getPolyline());
            routeResponse.setBusId(r.getBusId());
            routeResponse.setSchoolId(r.getSchool().getId());
            routeResponse.setStops(ConvertorUtils.convertToStopModel(r.getStops()));

            routeResponses.add(routeResponse);
        });
        return routeResponses;
    }
}
