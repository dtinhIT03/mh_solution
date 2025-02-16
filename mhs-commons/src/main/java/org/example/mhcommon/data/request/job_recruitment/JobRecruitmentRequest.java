package org.example.mhcommon.data.request.job_recruitment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.experimental.Accessors;
import org.example.mhcommon.data.model.Info;

import java.util.List;

@Data
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JobRecruitmentRequest {
    private String title;
    private String salary;
    private String level;
    private String career;
    private String jobDescription;
    private String jobRequirements;
    private String jobBenefits;
    private String workExperience;
    private String workType;
    private String workAddress;
    private String numberRecruits;
    private Long createdAt;
    private Long endTime;
    private String coverUrl;
    private List<Info> infos;
    private Integer createBy;
    private Integer deleteBy;
}
