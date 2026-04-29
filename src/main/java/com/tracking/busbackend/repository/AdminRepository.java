package com.tracking.busbackend.repository;

import com.tracking.busbackend.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByMobileOrEmail(String mobile, String email);
}
