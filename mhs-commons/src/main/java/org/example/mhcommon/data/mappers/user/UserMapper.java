package org.example.mhcommon.data.mappers.user;

import org.example.mhcommon.data.mappers.BaseMap;
import org.example.mhcommon.data.mappers.role.RoleMapper;
import org.example.mhcommon.data.request.user.UserRequest;
import org.example.mhcommon.data.response.user.UserResponse;
import org.example.mhscommons.data.tables.pojos.Permission;
import org.example.mhscommons.data.tables.pojos.Role;
import org.example.mhscommons.data.tables.pojos.User;
import org.example.mhscommons.data.tables.pojos.UserRole;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Collectors.*;

import static java.util.stream.Collectors.*;

@Mapper(componentModel = "spring")
public abstract class UserMapper extends BaseMap<UserRequest, UserResponse, User> {
    @Autowired
    RoleMapper roleMapper;
    public abstract UserResponse toResponse(User user, @Context List<Role> roles);
    @AfterMapping
    protected void afterResponse(@MappingTarget UserResponse userResponse,@Context List<Role> roles){
        userResponse.setRoles(roleMapper.toResponses(roles));
    }
    public List<UserResponse> toResponses(List<User> users
            , @Context List<UserRole> userRole
            ,@Context Map<Integer,Role> roleMap){
        Map<Integer, List<Role>> userRoleMap = userRole.stream()
                .filter(rp -> roleMap.containsKey(rp.getRoleId()))
                .collect(groupingBy(
                        UserRole::getUserId,
                        mapping(rp -> roleMap.get(rp.getRoleId()), toList())));
        return users.stream()
                .map(user -> toResponse(user, userRoleMap.getOrDefault(user.getId().longValue(), new ArrayList<>())))
                .collect(Collectors.toList());
    }
}
