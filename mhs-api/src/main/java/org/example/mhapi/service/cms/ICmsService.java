package org.example.mhapi.service.cms;

import org.example.mhcommon.data.constant.message.MessageResponse;
import org.example.mhcommon.data.model.paging.Page;
import org.example.mhcommon.data.model.SearchRequest;
import org.example.mhsconfig.config.exception.ApiException;

import org.springframework.security.core.Authentication;

public interface ICmsService<Rq, Rp, ID> {
    Rp insert(Rq request, Authentication authentication) throws ApiException;
    Rp findById(ID id,Authentication authentication) throws ApiException;
    Rp updateById(Rq request, ID id,Authentication authentication) throws ApiException;
    MessageResponse deleteById(ID id, Authentication authentication) throws ApiException;
    Page<Rp> search(SearchRequest searchRequest);
}
