package com.tracking.busbackend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class School {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String address;

    private String email;

    private String mobile;

    private String password;

    private Boolean isActive;

    @OneToMany(mappedBy = "school")
    private List<Parent> parents;

    @OneToMany(mappedBy = "school")
    private List<Route> routes;

    @OneToMany(mappedBy = "school")
    private List<Driver> drivers;
}
