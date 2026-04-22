package com.tracking.busbackend.repository;

import com.tracking.busbackend.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteRepo extends JpaRepository<Route, Long> {
    List<Route> findBySchoolId(Long schoolId);
    Route findByBusId(String busId);
}