package org.example.mhapi.service.cms.role;

import lombok.extern.slf4j.Slf4j;
import org.example.mhapi.service.cms.AbsCmsService;
import org.example.mhcommon.data.mappers.role.RoleMapper;
import org.example.mhcommon.data.model.SearchRequest;
import org.example.mhcommon.data.model.paging.Page;
import org.example.mhcommon.data.request.role.RoleRequest;
import org.example.mhcommon.data.request.role_permission.RolePermissionRequest;
import org.example.mhcommon.data.response.MessageResponse;
import org.example.mhcommon.data.response.role.RoleResponse;
import org.example.mhcommon.utils.CollectionUtils;
import org.example.mhscommons.data.tables.pojos.Permission;
import org.example.mhscommons.data.tables.pojos.Role;
import org.example.mhscommons.data.tables.pojos.RolePermission;
import org.example.mhsconfig.config.exception.ApiException;
import org.example.mhsrepository.repository.role.RoleRepositoryImp;
import org.example.mhsrepository.repository.role_permisson.RolePermissionRepositoryImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.example.mhcommon.data.constant.message.role.RoleConstant.ROLE_NOT_FOUND;

@Service
@Slf4j
public class RoleCmsServiceImp extends AbsCmsService<RoleRequest, RoleResponse, Role,Long, RoleRepositoryImp, RoleMapper> implements IRoleCmsService{
    @Autowired
    RolePermissionRepositoryImp repositoryImp;

    @Override
    protected String getPermissionCode() {
        return "role";
    }
    public RoleCmsServiceImp(RoleRepositoryImp repositoryImp,RoleMapper mapper){
        this.repository = repositoryImp;
        this.mapper = mapper;
    }

    @Override
    public Page<RoleResponse> search(SearchRequest searchRequest) {
        List<Role> roles = repository.search(searchRequest);
        List<Long> roleIds = CollectionUtils.extractField(roles,Role::getId);
        List<Integer> roleIdsInt = roleIds.stream().map(Long::intValue).collect(Collectors.toList());

        Long total = repository.count(searchRequest);
        List<RolePermission> rolePermissions = repositoryImp.getByRoleIds(roleIdsInt);
        Map<Long, Permission> permissionMap = repository.getPermissionsByRoleIds(roleIdsInt);
        List<RoleResponse> roleResponses = mapper.toResponses(roles,rolePermissions,permissionMap);
        return new Page<RoleResponse>()
                .setKey(searchRequest.getKeyword())
                .setTotal(total)
                .setPage(searchRequest.getPage())
                .setItems(roleResponses);
    }

    @Override
    public RoleResponse findById(Long id, Authentication authentication) {
        try {
            //check per, tim role, tim per, map sang res
            Set<String> actions = checkPermissionGet(authentication);
            Optional<Role> role = repository.findById(id);
            if(role.isEmpty()){
                throw new ApiException(ROLE_NOT_FOUND);
            }
            List<Permission> permissions = repository.getPermissions(id.intValue());
            RoleResponse roleResponse = mapper.toResponse(role.get(),permissions);
            roleResponse.setItemPermissions(actions);
            return roleResponse;
        }catch (ApiException e){
            log.error("[ERROR] findById {} " + e.getMessage());
            throw new ApiException(ROLE_NOT_FOUND);
        }
    }

    @Override
    public MessageResponse updatePermissions(RolePermissionRequest request,Authentication authentication) {
        checkPermissionUpdate(authentication);
        List<Permission> oldPermissions = repository.getPermissions(request.getRoleId());

        return updatePermissions(request,oldPermissions);
    }

    @Override
    public MessageResponse updatePermissions(RolePermissionRequest request, List<Permission> oldPermissions) {
        Set<Integer> oldperIds = CollectionUtils.collectToSet(oldPermissions,Permission::getId)
                .stream()
                .map(Long::intValue)
                .collect(Collectors.toSet());;

        //insert new Per not contains of old Per
        List<RolePermission> newRolePer = request.getPermissionIds()
                .stream().filter(newPerId -> !oldperIds.contains(newPerId))
                .map(newPerId -> new RolePermission()
                        .setRoleId(request.getRoleId())
                        .setPerId(request.getPerId()))
                .collect(Collectors.toList());
        //delete old Per not contains of new Per
        List<Integer> oldPerIdsDelete = oldperIds.stream()
                .filter(oldPer -> !request.getPermissionIds().contains(oldPer))
                .collect(Collectors.toList());

        repositoryImp.insert(newRolePer);

        repositoryImp.deleteByIds(oldPerIdsDelete,request.getRoleId());
        return new MessageResponse("update permission success!");
    }

    @Override
    public Set<String> getListActionCode(Long roleId) {
        try {
            //check per, tim role, tim per, map sang res
            Optional<Role> role = repository.findById(roleId);
            if(role.isEmpty()){
                throw new ApiException(ROLE_NOT_FOUND);
            }
            List<Permission> permissions = repository.getPermissions(roleId.intValue());
            return permissions.stream().map(per -> per.getActionCode()).collect(Collectors.toSet());
        }catch (ApiException e){
            log.error("[ERROR] findById {} " + e.getMessage());
            throw new ApiException(ROLE_NOT_FOUND);
        }    }
}
