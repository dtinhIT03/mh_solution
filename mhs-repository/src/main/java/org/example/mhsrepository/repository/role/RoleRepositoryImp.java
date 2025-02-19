package org.example.mhsrepository.repository.role;

import org.example.mhscommons.data.Tables;
import org.example.mhscommons.data.tables.pojos.Permission;
import org.example.mhscommons.data.tables.pojos.Role;
import org.example.mhscommons.data.tables.records.RoleRecord;
import org.example.mhsrepository.repository.AbsRepository;
import org.jooq.impl.TableImpl;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Repository
public class RoleRepositoryImp extends AbsRepository<RoleRecord,Role,Long> implements IRoleRepository{
    @Override
    protected TableImpl<RoleRecord> getTable() {
        return Tables.ROLE;
    }

    @Override
    public List<Permission> getPermissions(Integer RoleId) {
        return dslContext.select(Tables.PERMISSION.ID,Tables.PERMISSION.ACTION_CODE,Tables.PERMISSION.ACTION_NAME)
                .from(Tables.PERMISSION)
                .join(Tables.ROLE_PERMISSION)
                .on(Tables.PERMISSION.ID.eq(Tables.ROLE_PERMISSION.PER_ID.cast(Long.class)))
                .join(Tables.ROLE)
                .on(Tables.ROLE.ID.eq(Tables.ROLE_PERMISSION.ROLE_ID.cast(Long.class)))
                .where(Tables.ROLE.ID.eq(RoleId.longValue()))
                .fetchInto(Permission.class);
    }

    @Override
    public Map<Long, Permission> getPermissionsByRoleIds(List<Integer> RoleIds) {
        return dslContext.select(Tables.PERMISSION.ID,Tables.PERMISSION.ACTION_CODE,Tables.PERMISSION.ACTION_NAME)
                .from(Tables.PERMISSION)
                .join(Tables.ROLE_PERMISSION)
                .on(Tables.PERMISSION.ID.eq(Tables.ROLE_PERMISSION.PER_ID.cast(Long.class)))
                .join(Tables.ROLE)
                .on(Tables.ROLE.ID.eq(Tables.ROLE_PERMISSION.ROLE_ID.cast(Long.class)))
                .where(Tables.ROLE.ID.in(RoleIds))
                .fetchMap(Tables.PERMISSION.ID,Permission.class);
    }
}
