package com.tracking.busbackend.model.school;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModifySchoolReq {

    private Long schoolId;

    private String name;

    private String address;

    private String email;

    private String mobile;

    private String password;

    private Boolean isActive;
}
