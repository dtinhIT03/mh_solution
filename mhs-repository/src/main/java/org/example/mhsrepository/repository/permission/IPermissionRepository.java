package org.example.mhsrepository.repository.permission;

import org.example.mhscommons.data.tables.pojos.Permission;

import java.util.List;

public interface IPermissionRepository {
    //getList Per by list Id Per
    List<Permission> getPermissions(List<Integer> perIds);
    //get all Per
    List<Permission> getAllPermission();
}
