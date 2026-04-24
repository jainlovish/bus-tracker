package com.tracking.busbackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ParentLoginResp {

    private Long parentId;
    private String name;
    private Long schoolId;
}
