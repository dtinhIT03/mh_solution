package org.example.mhapi.service.api.job;

import lombok.extern.slf4j.Slf4j;
import org.example.mhcommon.data.mappers.job_recruitment.JobRecruitmentMapper;
import org.example.mhcommon.data.model.paging.Page;
import org.example.mhcommon.data.model.SearchRequest;
import org.example.mhcommon.data.request.job_recruitment.JobRecruitmentRequest;
import org.example.mhcommon.data.response.job_recruitment.JobRecruitmentResponse;
import org.example.mhsconfig.config.exception.ApiException;

import org.example.mhsrepository.repository.job.JobRepositoryImpl;
import org.example.mhapi.service.api.AbsService;
import org.example.mhscommons.data.tables.pojos.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.example.mhcommon.data.constant.message.job_recruitment.JobRecruitmentMessageConstant.JOB_NOT_FOUND;

@Service
@Slf4j
public class JobRecruitmentServiceImp extends AbsService<JobRecruitmentRequest, JobRecruitmentResponse, Job,Integer, JobRepositoryImpl, JobRecruitmentMapper>
        implements IJobRecruitmentService{

    @Autowired
    public JobRecruitmentServiceImp(JobRepositoryImpl jobRepository, JobRecruitmentMapper mapper){
        this.repository = jobRepository;
        this.mapper = mapper;
    }

    @Override
    public JobRecruitmentResponse findById(Integer integer) {
        try{
            Job job = repository.findById(integer)
                    .orElseThrow(() -> new ApiException(JOB_NOT_FOUND, 404));
            JobRecruitmentResponse response = mapper.toResponse(job);
            return response;
        }catch (ApiException apiException){
            log.error("[ERROR] findById {} " + apiException.getMessage());
            throw new ApiException(apiException.getMessage(), apiException.getCode());
        }
    }

    @Override
    public Page<JobRecruitmentResponse> search(SearchRequest searchRequest) {
        try{
            List<Job> jobList =  repository.search(searchRequest);
            Long total = repository.count(searchRequest);
            List<JobRecruitmentResponse> jobRecruitmentResponses = mapper.toResponses(jobList);
            return new Page<JobRecruitmentResponse>(total,searchRequest,jobRecruitmentResponses);
        }catch (Exception exception){
            log.error("[ERROR] search {} " + exception.getMessage());
            return new Page<JobRecruitmentResponse>()
                    .setTotal(0L)
                    .setPage(searchRequest.getPage())
                    .setItems(new ArrayList<>());
        }


    }
}
