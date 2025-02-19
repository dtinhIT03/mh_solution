package org.example.mhapi.controller.cms.role;

import org.example.mhapi.service.cms.role.RoleCmsServiceImp;
import org.example.mhcommon.data.model.DfResponse;
import org.example.mhcommon.data.model.SearchRequest;
import org.example.mhcommon.data.model.paging.Page;
import org.example.mhcommon.data.request.role.RoleRequest;
import org.example.mhcommon.data.request.role_permission.RolePermissionRequest;
import org.example.mhcommon.data.response.MessageResponse;
import org.example.mhcommon.data.response.role.RoleResponse;
import org.example.mhsrepository.repository.role.RoleRepositoryImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cms/role")
public class RoleCmsController {
    //detail,insert,update,delete,search,updatePermission
    @Autowired
    RoleCmsServiceImp roleCmsServiceImp;
    @GetMapping("/{id}")
    public  DfResponse<RoleResponse> detail(@PathVariable Long id, Authentication authentication){
        return DfResponse.ok(roleCmsServiceImp.findById(id,authentication));
    }

    @PostMapping
    public DfResponse<RoleResponse> insert(@RequestBody RoleRequest request,Authentication authentication){
        return DfResponse.ok(roleCmsServiceImp.insert(request,authentication));
    }

    @PutMapping("/{id}")
    public DfResponse<RoleResponse> update(@RequestBody RoleRequest request,
            @PathVariable Long id
            ,Authentication authentication){
        return DfResponse.ok(roleCmsServiceImp.updateById(request,id,authentication));
    }
    @DeleteMapping("/{id}")
    public DfResponse<MessageResponse> delete(@PathVariable Long id,Authentication authentication){
        return DfResponse.ok(roleCmsServiceImp.deleteById(id, authentication));
    }

    @PostMapping("/search")
    public DfResponse<Page<RoleResponse>> search(@RequestBody SearchRequest searchRequest){
        return DfResponse.ok(roleCmsServiceImp.search(searchRequest));
    }
    @PutMapping("/updatePers")
    public DfResponse<MessageResponse> updatePermission(@RequestBody RolePermissionRequest rolePermissionRequest,
                                                        Authentication authentication){
        return DfResponse.ok(roleCmsServiceImp.updatePermissions(rolePermissionRequest,authentication));
    }
}
