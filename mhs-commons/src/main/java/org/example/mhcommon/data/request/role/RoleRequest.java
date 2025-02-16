package org.example.mhcommon.data.request.role;

import lombok.Data;

@Data
public class RoleRequest {
    private Long id;
    private String roleName;
    private String roleCode;
    private String description;
    private String deletedAt;
}
