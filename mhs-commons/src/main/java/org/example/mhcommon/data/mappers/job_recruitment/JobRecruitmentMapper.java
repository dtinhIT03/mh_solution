package org.example.mhcommon.data.mappers.job_recruitment;


import lombok.SneakyThrows;
import org.example.mhcommon.core.json.JsonArray;
import org.example.mhcommon.data.mappers.BaseMap;
import org.example.mhcommon.data.model.Info;
import org.example.mhcommon.data.request.job_recruitment.JobRecruitmentRequest;
import org.example.mhcommon.data.response.job_recruitment.JobRecruitmentResponse;
import org.example.mhscommons.data.tables.pojos.Job;
import org.jooq.JSON;
import org.mapstruct.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static org.example.mhcommon.config.JsonMapper.getObjectMapper;

import static java.time.temporal.ChronoField.INSTANT_SECONDS;
import static org.example.mhcommon.utils.SeoUtils.generatedSeoId;

@Mapper(componentModel = "spring")
public abstract class JobRecruitmentMapper extends BaseMap<JobRecruitmentRequest, JobRecruitmentResponse, Job> {
    @Named("toPOJO") //dat ten
    @Mapping(
            target = "info", ignore = true
    )
    public abstract Job toPOJO(JobRecruitmentRequest request);

    @Named("toResponse")
    @Mapping(
            target = "infos", ignore = true
    )
    public abstract JobRecruitmentResponse toResponse(Job pojo);

    @SneakyThrows
    @AfterMapping
    protected void afterToPojo(@MappingTarget Job jobRecruitment, JobRecruitmentRequest request) {
        if (request.getInfos() != null) {
            final String info = getObjectMapper().writeValueAsString(request.getInfos());
            jobRecruitment.setInfo(JSON.valueOf(info));
        }
    }

    @SneakyThrows
    @AfterMapping
    protected void afterToResponse(@MappingTarget JobRecruitmentResponse response, Job jobRecruitment) {
        if (jobRecruitment.getInfo() != null) {
            List<Info> infoResponses = new JsonArray(jobRecruitment.getInfo().data()).mapTo(Info.class);
            response.setInfos(infoResponses);
        } else {
            response.setInfos(new ArrayList<>());
        }
    }
    @AfterMapping
    protected void afterResponse(Job jobRecruitment, @MappingTarget JobRecruitmentResponse response) {
        response.setSeoId(generatedSeoId(response.getTitle(), String.valueOf(response.getId())));
    }

    protected Long map(LocalDateTime localDateTime) {
        return localDateTime == null ? null :
                localDateTime.atZone(ZoneId.systemDefault()).getLong(INSTANT_SECONDS) * 1000;
    }

    protected LocalDateTime map(Long time) {
        return time == null ? null :
                Instant.ofEpochMilli(time).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

}
