package org.example.mhsrepository.repository.role_permisson;

import org.example.mhscommons.data.tables.pojos.RolePermission;

import java.util.List;

public interface IRolePermissionRepository {
    List<String> getPermission(String permissionCode, List<String> roles);
    List<RolePermission> getByRoleIds(List<Integer> roleIds);
    Integer deleteByIds(List<Integer> perIds,Integer roleId);

}
