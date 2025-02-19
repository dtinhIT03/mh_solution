package org.example.mhsrepository.repository.permission;

import org.example.mhscommons.data.Tables;
import org.example.mhscommons.data.tables.pojos.Permission;
import org.example.mhscommons.data.tables.records.PermissionRecord;
import org.example.mhsrepository.repository.AbsRepository;
import org.jooq.impl.TableImpl;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class PermissionRepositoryImp extends AbsRepository<PermissionRecord,Permission,Long> implements IPermissionRepository {
    @Override
    protected TableImpl<PermissionRecord> getTable() {
        return Tables.PERMISSION;
    }

    @Override
    public List<Permission> getPermissions(List<Integer> perIds) {

        return dslContext.select(Tables.PERMISSION)
                .from(getTable())
                .where(Tables.PERMISSION.ID.in(perIds))
                .fetchInto(Permission.class);
    }

    @Override
    public List<Permission> getAllPermission() {
        return dslContext.select(Tables.PERMISSION)
                .from(getTable())
                .fetchInto(Permission.class);
    }
}
