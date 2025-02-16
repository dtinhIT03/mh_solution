package org.example.mhsrepository.repository.role_permisson;

import org.example.mhscommons.data.Tables;
import org.example.mhsrepository.repository.AbsRepository;
import org.example.mhscommons.data.tables.pojos.RolePermission;
import org.example.mhscommons.data.tables.records.RolePermissionRecord;
import org.jooq.Record1;
import org.jooq.Select;
import org.jooq.impl.TableImpl;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class RolePermissionRepositoryImp extends AbsRepository<RolePermissionRecord, RolePermission,Long>
        implements IRolePermissionRepository {
    @Override
    protected TableImpl<RolePermissionRecord> getTable() {
        return Tables.ROLE_PERMISSION;
    }

    @Override
    public List<String> getPermission(String permissionCode, List<String> roles) {
        return dslContext.select(Tables.PERMISSION.ACTION_CODE)
                .from(Tables.PERMISSION)
                .join(Tables.ROLE_PERMISSION)
                .on(Tables.PERMISSION.ID.eq(Tables.ROLE_PERMISSION.PER_ID.cast(Long.class)))
                .join(Tables.ROLE)
                .on(Tables.ROLE.ID.eq(Tables.ROLE_PERMISSION.ROLE_ID.cast(Long.class)))
                .where(Tables.ROLE.ROLE_NAME.in(roles).and(Tables.PERMISSION.ACTION_CODE.like(permissionCode)))
                .fetchInto(String.class);
    }

    @Override
    public List<RolePermission> getByRoleIds(List<Integer> roleIds) {
        return dslContext.selectFrom(Tables.ROLE_PERMISSION)
                .where(Tables.ROLE_PERMISSION.ROLE_ID.in(roleIds))
                .fetchInto(RolePermission.class);
    }

    @Override
    public Integer deleteByIds(List<Integer> perIds, Integer roleId) {
        return dslContext.delete(Tables.ROLE_PERMISSION)
                .where(Tables.ROLE_PERMISSION.PER_ID.in(perIds).and(Tables.ROLE_PERMISSION.ROLE_ID.eq(roleId)))
                .execute();
    }
}
