package vn.com.la.web.rest.vm.request;

public class FinishJobRequestVM extends AbstractRequestVM{
    private Long jobId;

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }
}
