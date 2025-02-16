package org.example.mhapi.controller.api.job;


import org.example.mhapi.service.api.job.IJobRecruitmentService;
import org.example.mhcommon.data.model.DfResponse;
import org.example.mhcommon.data.model.SearchRequest;
import org.example.mhcommon.data.model.paging.Page;
import org.example.mhcommon.data.response.job_recruitment.JobRecruitmentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/public/job-recruitment")
public class JobRecruitmentController {
    private IJobRecruitmentService service;

    @Autowired
    public JobRecruitmentController(IJobRecruitmentService service){
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<DfResponse<JobRecruitmentResponse>> detail(
            @PathVariable("id") int id
    ) {
        return DfResponse
                .okEntity(service.findById(id));
    }

    //api search
    @PostMapping("/search")
    public ResponseEntity<DfResponse<Page<JobRecruitmentResponse>>> search
            (@RequestBody SearchRequest searchRequest){
        return DfResponse
                .okEntity(service.search(searchRequest));
    }

}
