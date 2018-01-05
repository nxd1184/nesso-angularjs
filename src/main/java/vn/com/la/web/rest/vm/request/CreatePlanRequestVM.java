package vn.com.la.web.rest.vm.request;

import javax.validation.constraints.NotNull;

public class CreatePlanRequestVM extends AbstractRequestVM{

    @NotNull
    private Long jobId;

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }
}
