package org.example.mhapi.service.api;


import org.example.mhcommon.data.model.paging.Page;
import org.example.mhcommon.data.model.SearchRequest;

import java.util.List;

public interface IService <Rq, Rp, ID>{
    Rp insert(Rq request);
    String insert(List<Rq> requestList);
    Rp update(ID id,Rq request);
    Rp findById(ID id);
    Page<Rp> search(SearchRequest searchRequest);
    Rp delete(ID id);
}
