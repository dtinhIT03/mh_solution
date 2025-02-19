package org.example.mhsrepository.repository.role;

import org.example.mhscommons.data.tables.pojos.Permission;

import java.util.List;
import java.util.Map;

public interface IRoleRepository {
    //get Per by RoleId
    List<Permission> getPermissions(Integer RoleId);
    //get Map<integer,permission> by List RoleId
    Map<Long,Permission> getPermissionsByRoleIds(List<Integer> RoleIds);
}
