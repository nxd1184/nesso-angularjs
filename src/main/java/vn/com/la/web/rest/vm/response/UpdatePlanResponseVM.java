package vn.com.la.web.rest.vm.response;

import vn.com.la.service.dto.JobDTO;

public class UpdatePlanResponseVM extends AbstractResponseVM{

    private JobDTO job;

    public JobDTO getJob() {
        return job;
    }

    public void setJob(JobDTO job) {
        this.job = job;
    }
}
