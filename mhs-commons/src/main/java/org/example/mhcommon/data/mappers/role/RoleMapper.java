package org.example.mhcommon.data.mappers.role;

import org.example.mhcommon.data.mappers.BaseMap;
import org.example.mhcommon.data.mappers.permission.PermissionMapper;
import org.example.mhcommon.data.request.role.RoleRequest;
import org.example.mhcommon.data.response.permission.PermissionResponse;
import org.example.mhcommon.data.response.role.RoleResponse;
import org.example.mhcommon.data.response.user.UserResponse;
import org.example.mhscommons.data.tables.pojos.Permission;
import org.example.mhscommons.data.tables.pojos.Role;
import org.example.mhscommons.data.tables.pojos.RolePermission;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Mapper(componentModel = "spring")
public abstract class RoleMapper extends BaseMap<RoleRequest, RoleResponse, Role> {
    @Autowired
    PermissionMapper permissionMapper;
    public abstract RoleResponse toResponse(Role role, @Context List<Permission> permissions);
    @AfterMapping
    protected void afterResponse(@MappingTarget RoleResponse roleResponse, @Context List<Permission> permissions){
        roleResponse.setPermissions(permissionMapper.toResponses(permissions));
    }

    public List<RoleResponse> toResponses(List<Role> roles,
                                          @Context List<RolePermission> rolePermissions,
                                          @Context Map<Long, Permission> permissionMap){

        Map<Integer, List<Permission>> rolePermissionMap = rolePermissions.stream()
                .filter(rp -> permissionMap.containsKey(rp.getPerId().intValue()))
                .collect(groupingBy(
                        RolePermission::getRoleId,
                        mapping(rp -> permissionMap.get(rp.getPerId().intValue()), toList())));

        return roles.stream()
                .map(role -> toResponse(role, rolePermissionMap.getOrDefault(role.getId(), new ArrayList<>())))
                .collect(Collectors.toList());
    }
}
