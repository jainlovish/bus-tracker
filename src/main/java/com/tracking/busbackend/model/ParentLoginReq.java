package com.tracking.busbackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ParentLoginReq {
    private String username; // email OR mobile
    private String password;
}
