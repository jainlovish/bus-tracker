package com.tracking.busbackend.model.parent;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParentResponse {

    private Long parentId;

    private String name;

    private String email;

    private String mobile;

    private String password;

    private Boolean isActive;

    private Long schoolId;
}
