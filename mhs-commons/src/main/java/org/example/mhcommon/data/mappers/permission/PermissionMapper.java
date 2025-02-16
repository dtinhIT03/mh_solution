package org.example.mhcommon.data.mappers.permission;

import org.example.mhcommon.data.mappers.BaseMap;
import org.example.mhcommon.data.request.permission.PermissionRequest;
import org.example.mhcommon.data.response.permission.PermissionResponse;
import org.example.mhscommons.data.tables.pojos.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class PermissionMapper extends BaseMap<PermissionRequest, PermissionResponse, Permission> {
}
