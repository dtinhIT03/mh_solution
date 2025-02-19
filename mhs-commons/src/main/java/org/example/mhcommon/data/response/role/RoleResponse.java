package org.example.mhcommon.data.response.role;

import lombok.Data;
import org.example.mhcommon.data.response.BaseResponse;
import org.example.mhcommon.data.response.permission.PermissionResponse;
import org.example.mhscommons.data.tables.pojos.Permission;
import org.example.mhscommons.data.tables.pojos.Role;

import java.util.List;

@Data
public class RoleResponse extends BaseResponse {
    private Long id;
    private String roleName;
    private String roleCode;
    private String description;
    private String deletedAt;
    private List<PermissionResponse> permissions;

}
