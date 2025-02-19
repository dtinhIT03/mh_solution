package org.example.mhapi.service.cms.permission;

import org.example.mhapi.service.api.AbsService;
import org.example.mhapi.service.cms.AbsCmsService;
import org.example.mhcommon.data.mappers.permission.PermissionMapper;
import org.example.mhcommon.data.model.SearchRequest;
import org.example.mhcommon.data.model.paging.Page;
import org.example.mhcommon.data.request.permission.PermissionRequest;
import org.example.mhcommon.data.response.permission.PermissionResponse;
import org.example.mhcommon.utils.CollectionUtils;
import org.example.mhscommons.data.tables.pojos.Permission;
import org.example.mhsrepository.repository.permission.IPermissionRepository;
import org.example.mhsrepository.repository.permission.PermissionRepositoryImp;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PermissionCmsServiceImp extends AbsCmsService<PermissionRequest, PermissionResponse, Permission,
        Long, PermissionRepositoryImp, PermissionMapper> implements IPermissionCmsService {
    public PermissionCmsServiceImp(PermissionRepositoryImp repositoryImp,PermissionMapper mapper) {
        this.mapper = mapper;
        this.repository = repositoryImp;
    }

    @Override
    protected String getPermissionCode() {
        return "permission";
    }

    @Override
    public Page<PermissionResponse> search(SearchRequest searchRequest) {
        List<Permission> permissions =  repository.search(searchRequest);
        Long total = repository.count(searchRequest);
        List<PermissionResponse> permissionResponses = mapper.toResponses(permissions);
        List<Long> perIdsLong = CollectionUtils. extractField(permissions, Permission::getId);
        List<Integer> perIds = perIdsLong.stream().map(Long::intValue).collect(Collectors.toList());
        List<Permission> detailPermission = repository.getPermissions(perIds);
        List<PermissionResponse> responses = mapper.toResponses(detailPermission);

        return new Page<PermissionResponse>()
                .setTotal(total)
                .setItems(responses);
    }



    @Override
    public List<PermissionResponse> getAll() {
        List<Permission> permissions = repository.getAllPermission();
        return permissions.stream()
                .map(permission -> mapper.toResponse(permission))
                .collect(Collectors.toList());
    }

}
