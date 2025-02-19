package org.example.mhapi.controller.cms.permission;

import org.example.mhapi.service.cms.permission.PermissionCmsServiceImp;
import org.example.mhcommon.data.model.DfResponse;
import org.example.mhcommon.data.model.SearchRequest;
import org.example.mhcommon.data.model.paging.Page;
import org.example.mhcommon.data.request.permission.PermissionRequest;
import org.example.mhcommon.data.response.MessageResponse;
import org.example.mhcommon.data.response.permission.PermissionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cms/permission")
public class PermissionCmsController {
    @Autowired
    private PermissionCmsServiceImp permissionCmsServiceImp;
    //search,getbyId,insert,update,delete,getAll
    @PostMapping("/search")
    public ResponseEntity<DfResponse<Page<PermissionResponse>>> search(@RequestBody SearchRequest searchRequest){
        return DfResponse.okEntity(permissionCmsServiceImp.search(searchRequest));
    }
    @GetMapping("/{id}")
    public ResponseEntity<DfResponse<PermissionResponse>> findById(@PathVariable Long id,
                                                                   Authentication authentication){
        return DfResponse.okEntity(permissionCmsServiceImp.findById(id,authentication));
    }
    @PostMapping
    public ResponseEntity<DfResponse<PermissionResponse>> insert(@RequestBody PermissionRequest permissionRequest,Authentication authentication){
        return DfResponse.okEntity(permissionCmsServiceImp.insert(permissionRequest,authentication));
    }
    @PutMapping("/{id}")
    public ResponseEntity<DfResponse<PermissionResponse>> update(@PathVariable Long id ,
            @RequestBody PermissionRequest permissionRequest,Authentication authentication){
        return DfResponse.okEntity(permissionCmsServiceImp.updateById(permissionRequest,id,authentication));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<DfResponse<MessageResponse>> delete(
            @PathVariable("id") Long id,
            Authentication authentication
    ){
        return DfResponse.okEntity(permissionCmsServiceImp.deleteById(id,authentication));
    }
    @GetMapping("/all")
    public ResponseEntity<DfResponse<List<PermissionResponse>>> getAll(){
        return DfResponse
                .okEntity(permissionCmsServiceImp.getAll());
    }
}
