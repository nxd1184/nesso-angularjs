package vn.com.la.service.dto.param;

public class GetUserJobDetailParamDTO {
    private Long jobTeamUserId;
    private Long jobId;

    public Long getJobTeamUserId() {
        return jobTeamUserId;
    }

    public void setJobTeamUserId(Long jobTeamUserId) {
        this.jobTeamUserId = jobTeamUserId;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }
}
