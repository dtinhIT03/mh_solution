package org.example.mhapi.service.cms.role;

import org.example.mhcommon.data.request.role_permission.RolePermissionRequest;
import org.example.mhcommon.data.response.MessageResponse;
import org.example.mhcommon.data.response.role.RoleResponse;
import org.example.mhscommons.data.tables.pojos.Permission;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Set;

public interface IRoleCmsService {
    RoleResponse findById(Long id, Authentication authentication);
    MessageResponse updatePermissions(RolePermissionRequest request,Authentication authentication);
    MessageResponse updatePermissions(RolePermissionRequest request, List<Permission> oldPermissions);
    Set<String> getListActionCode(Long roleId);
}
