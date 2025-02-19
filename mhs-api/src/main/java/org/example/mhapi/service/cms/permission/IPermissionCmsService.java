package org.example.mhapi.service.cms.permission;

import org.example.mhcommon.data.model.SearchRequest;
import org.example.mhcommon.data.model.paging.Page;
import org.example.mhcommon.data.response.permission.PermissionResponse;

import java.util.List;

public interface IPermissionCmsService {
    Page<PermissionResponse> search(SearchRequest searchRequest);
    List<PermissionResponse> getAll();
}
