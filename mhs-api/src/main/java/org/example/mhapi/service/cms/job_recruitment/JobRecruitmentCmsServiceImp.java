package org.example.mhapi.service.cms.job_recruitment;

import org.example.mhapi.service.cms.AbsCmsService;
import org.example.mhcommon.data.mappers.job_recruitment.JobRecruitmentMapper;
import org.example.mhcommon.data.request.job_recruitment.JobRecruitmentRequest;
import org.example.mhcommon.data.response.job_recruitment.JobRecruitmentResponse;
import org.example.mhscommons.data.tables.pojos.Job;
import org.example.mhsrepository.repository.job.JobRepositoryImpl;
import org.springframework.stereotype.Service;

@Service
public class JobRecruitmentCmsServiceImp extends AbsCmsService<JobRecruitmentRequest, JobRecruitmentResponse, Job, Integer,
        JobRepositoryImpl, JobRecruitmentMapper> {
    @Override
    protected String getPermissionCode() {
        return "job_recruitment";
    }
    public JobRecruitmentCmsServiceImp(JobRepositoryImpl jobRepository, JobRecruitmentMapper mapper){
        this.mapper=mapper;
        this.repository=jobRepository;
    }

}
