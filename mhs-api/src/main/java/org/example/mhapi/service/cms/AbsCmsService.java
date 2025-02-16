package org.example.mhapi.service.cms;

import lombok.extern.slf4j.Slf4j;
import org.example.mhcommon.data.constant.message.MessageResponse;
import org.example.mhcommon.data.mappers.BaseMap;
import org.example.mhcommon.data.model.paging.Page;
import org.example.mhcommon.data.model.SearchRequest;
import org.example.mhcommon.data.model.user.UserAuthorDTO;
import org.example.mhcommon.data.response.BaseResponse;
import org.example.mhsconfig.config.exception.ApiException;
import org.example.mhcommon.data.constant.ActionConstant;

import org.example.mhsrepository.repository.AbsRepository;
import org.example.mhsrepository.repository.role_permisson.RolePermissionRepositoryImp;
import org.example.mhsconfig.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.example.mhcommon.data.constant.message.BaseConstant.UPDATE_FAIL;


@Slf4j
public abstract class AbsCmsService<Rq , Rp extends BaseResponse, Po,ID,Repo extends AbsRepository<?,Po,ID>
        ,Mp extends BaseMap<Rq,Rp,Po>>
        implements ICmsService<Rq,Rp,ID> {
    protected Mp mapper;
    protected Repo repository;
    @Autowired
    RolePermissionRepositoryImp rolePermissionRepositoryImp;

    @Override
    public Rp insert(Rq request, Authentication authentication) throws ApiException {
        try{
            Set<String> actions = checkPermissionInsert(authentication);
            Po pojo = mapper.toPOJO(request);
            Optional<Po> optional =  repository.insertReturning(pojo);
            Rp response = mapper.toResponse(optional.orElse(null));
            if(response != null){
                response.setItemPermissions(actions);
                return response;
            } else{
                throw new ApiException("resource not found", 404);
            }
        }catch (ApiException apiException){
            log.error("[ERROR] insert {} " + apiException.getMessage());
            throw new ApiException(apiException.getMessage(), apiException.getCode());
        }
    }

    @Override
    public Rp findById(ID id, Authentication authentication) throws ApiException {
        Set<String> actions = checkPermissionGet(authentication);
        return getById(id, actions);
    }

    @Override
    public Rp updateById(Rq request, ID id, Authentication authentication) throws ApiException {
        try{
            Set<String> actions = checkPermissionUpdate(authentication);
            Po pojo = mapper.toPOJO(request);
            int isUpdated =  repository.update(pojo,id);
            if(isUpdated != 1){
                throw new ApiException(UPDATE_FAIL);
            }else{
                return getById(id,actions);
            }
        }catch (Exception apiException){
            log.error("[ERROR] update {}", apiException.getMessage());
            throw new ApiException(UPDATE_FAIL);
        }

    }
    private Rp getById(ID id,Set<String> actions){
        try{
            Optional<Po> pojoOp = repository.findById(id);
            Rp response = mapper.toResponse(pojoOp.orElse(null));
            if(response != null){
                response.setItemPermissions(actions);
                return response;
            }else {
                throw new ApiException("resource not found", 404);
            }
        } catch (ApiException apiException) {
            log.error("[ERROR] getById {} " + apiException.getMessage());
            throw new ApiException(apiException.getMessage(), apiException.getCode());
        }

    }

    @Override
    public MessageResponse deleteById(ID id, Authentication authentication) throws ApiException {
        return null;
    }

    @Override
    public Page<Rp> search(SearchRequest searchRequest) {
        return null;
    }
    private Set<String> checkPermitAction(String view,String message,Authentication authentication){
        Set<String> actions =  getPermitAction(authentication);
        if(!actions.contains(view)){
            throw new ApiException(message);
        }
        return actions;
    }

    protected abstract String getPermissionCode();
    protected Set<String> getPermitAction(Authentication authentication){
        UserAuthorDTO userAuthorDTO = SecurityUtils
                .extractUser(authentication);
        return rolePermissionRepositoryImp.getPermission( getPermissionCode(),userAuthorDTO.getRoles())
                .stream()
                .map(s -> {
                    String[] split = s.split("\\.");
                    if(split.length<2) return null;
                    return split[1];
                }).filter(Objects::nonNull)
                .collect(Collectors.toSet());

    }
    protected Set<String> checkPermissionInsert(Authentication authentication){
        return checkPermitAction(ActionConstant.ADD, "Bạn không có quyền thêm.", authentication);
    }
    protected Set<String> checkPermissionGet(Authentication authentication){
        return checkPermitAction(ActionConstant.VIEW, "Bạn không có quyền lấy dữ liệu này.", authentication);
    }
    protected Set<String> checkPermissionUpdate(Authentication authentication){
        return checkPermitAction(ActionConstant.UPDATE, "Bạn không có quyền chỉnh sửa.", authentication);
    }
    protected Set<String> checkPermissionDelete(Authentication authentication){
        return checkPermitAction(ActionConstant.DELETE, "Bạn không có quyền xóa.", authentication);
    }
}
