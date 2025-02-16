package org.example.mhapi.controller.cms.job_recruitment;

import org.example.mhapi.service.cms.job_recruitment.JobRecruitmentCmsServiceImp;
import org.example.mhcommon.data.model.DfResponse;
import org.example.mhcommon.data.response.job_recruitment.JobRecruitmentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
