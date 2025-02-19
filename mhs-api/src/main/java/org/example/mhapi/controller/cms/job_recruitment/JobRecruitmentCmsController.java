package org.example.mhapi.controller.cms.job_recruitment;

import org.example.mhapi.service.cms.job_recruitment.JobRecruitmentCmsServiceImp;
import org.example.mhcommon.data.model.DfResponse;
import org.example.mhcommon.data.model.SearchRequest;
import org.example.mhcommon.data.model.paging.Page;
import org.example.mhcommon.data.request.job_recruitment.JobRecruitmentRequest;
import org.example.mhcommon.data.response.MessageResponse;
import org.example.mhcommon.data.response.job_recruitment.JobRecruitmentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cms/job-recruitment")
public class JobRecruitmentCmsController {
    @Autowired
    private JobRecruitmentCmsServiceImp service;
    @GetMapping("/{id}")
    public ResponseEntity<DfResponse<JobRecruitmentResponse>> detail(@PathVariable("id") Integer id,
                                                                     Authentication authentication){
        return DfResponse
                .okEntity(service.findById(id,authentication));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DfResponse<JobRecruitmentResponse>> update(@RequestBody JobRecruitmentRequest request,
                                                                     @PathVariable("id") Integer id,
                                                                     Authentication authentication){
        return DfResponse
                .okEntity(service.updateById(request,id,authentication));
    }
    @PostMapping
    public ResponseEntity<DfResponse<JobRecruitmentResponse>> insert(@RequestBody JobRecruitmentRequest request,
                                                                     Authentication authentication){
        return DfResponse
                .okEntity(service.insert(request,authentication));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<DfResponse<MessageResponse>> delete(@PathVariable("id") Integer id,
                                                              Authentication authentication){
        return DfResponse
                .okEntity(service.deleteById(id,authentication));
    }
    @PostMapping("/search")
    public ResponseEntity<DfResponse<Page<JobRecruitmentResponse>>> search(@RequestBody SearchRequest request){
        return DfResponse
                .okEntity(service.search(request));
    }
}
