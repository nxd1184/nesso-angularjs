package vn.com.la.web.rest.vm.response;

import vn.com.la.domain.enumeration.JobStatusEnum;
import vn.com.la.service.dto.JobDTO;
import vn.com.la.service.dto.JobTeamDTO;
import vn.com.la.service.dto.TaskDTO;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class JobPlanDetailResponseVM extends AbstractResponseVM{
    private JobDTO job;

    public JobDTO getJob() {
        return job;
    }

    public void setJob(JobDTO job) {
        this.job = job;
    }

}
