package vn.com.la.web.rest.vm.response;

import vn.com.la.service.dto.JobDTO;
import vn.com.la.service.dto.JobTeamUserDTO;

public class UserJobDetailResponseVM {
    private JobTeamUserDTO jobTeamUser;
    private JobDTO job;

    public JobTeamUserDTO getJobTeamUser() {
        return jobTeamUser;
    }

    public void setJobTeamUser(JobTeamUserDTO jobTeamUser) {
        this.jobTeamUser = jobTeamUser;
    }

    public JobDTO getJob() {
        return job;
    }

    public void setJob(JobDTO job) {
        this.job = job;
    }
}
