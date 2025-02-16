package org.example.mhsrepository.repository.user;

import org.example.mhcommon.data.request.user.UserRequest;
import org.example.mhcommon.data.response.user.UserResponse;
import org.example.mhscommons.data.tables.pojos.Role;
import org.example.mhscommons.data.tables.pojos.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IUserRepository {
    /**
     * method findUserByEmail
     * params : String
     * return Optional<User>
     */
    Optional<User> findUserByEmail(String email);
    /**
     * method findRoleNameByUserId
     * params : userId
     * return List<String> (role)
     */
    List<String> findRoleNameByUserId(Long userId);
    /**
     * method getRoles
     * params : userId
     * return List<Role>
     */
    List<Role> getRoles(Integer userId);
    /**
     * method getRoleByUserIds
     * params List<Integer>
     * return Map<integer,Role>
     */
    Map<Long,Role> getRoleByUserIds(List<Integer> userIds);

}
