package com.tracking.busbackend.repository;

import com.tracking.busbackend.entity.Stop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StopRepo extends JpaRepository<Stop, Long> {
    List<Stop> findByRouteIdOrderBySequenceNo(Long routeId);
}
