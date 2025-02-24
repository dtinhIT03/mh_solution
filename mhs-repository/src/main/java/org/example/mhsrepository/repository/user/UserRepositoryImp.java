package org.example.mhsrepository.repository.user;

import org.example.mhcommon.data.request.user.UserRequest;
import org.example.mhcommon.data.response.user.UserResponse;
import org.example.mhscommons.data.Tables;
import org.example.mhscommons.data.tables.pojos.Role;
import org.example.mhscommons.data.tables.pojos.User;
import org.example.mhscommons.data.tables.records.UserRecord;
import org.example.mhsrepository.repository.AbsRepository;
import org.jooq.Record1;
import org.jooq.Select;
import org.jooq.impl.TableImpl;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserRepositoryImp extends AbsRepository<UserRecord, User,Long> implements IUserRepository{


    @Override
    protected TableImpl<UserRecord> getTable() {
        return Tables.USER;
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return dslContext.select(Tables.USER)
                .from(Tables.USER)
                .where(Tables.USER.EMAIL.eq(email))
                .fetchOptionalInto(User.class);
    }

    @Override
    public List<String> findRoleNameByUserId(Long userId) {
        return dslContext.select(Tables.ROLE.ROLE_NAME)
                .from(Tables.ROLE)
                .join(Tables.USER_ROLE)
                .on(Tables.ROLE.ID.eq( Tables.USER_ROLE.ROLE_ID.cast(Long.class)))
                .join(Tables.USER)
                .on(Tables.USER.ID.eq( Tables.USER_ROLE.USER_ID.cast(Long.class)))
                .where(Tables.USER.ID.eq(Long.valueOf(userId)))
                .fetchInto(String.class);

    }

    @Override
    public List<Role> getRoles(Long userId) {
        return dslContext.select(Tables.ROLE)
                .from(Tables.ROLE)
                .join(Tables.USER_ROLE)
                .on(Tables.ROLE.ID.eq(Tables.USER_ROLE.ROLE_ID.cast(Long.class)))
                .join(Tables.USER)
                .on(Tables.USER.ID.eq(Tables.USER_ROLE.USER_ID.cast(Long.class)))
                .where(Tables.USER.ID.eq(userId))
                .fetchInto(Role.class);
    }

    @Override
    public Map<Long, Role> getRoleByUserIds(List<Integer> userIds) {
        return dslContext.select(Tables.ROLE.ID, Tables.ROLE.ROLE_NAME, Tables.ROLE.ROLE_CODE, Tables.ROLE.DESCRIPTION)
                .from(Tables.ROLE)
                .join(Tables.USER_ROLE)
                .on(Tables.ROLE.ID.eq((Select<? extends Record1<Long>>) Tables.USER_ROLE.ROLE_ID))
                .join(Tables.USER)
                .on(Tables.USER.ID.eq((Select<? extends Record1<Long>>) Tables.USER_ROLE.USER_ID))
                .where(Tables.USER.ID.in(userIds))
                .fetchMap(Tables.ROLE.ID, Role.class);
    }


}
