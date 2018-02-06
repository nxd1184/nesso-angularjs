package vn.com.la.service.dto.param;

public class AdjustFilesParamDTO {
    private Long jobId;
    private Long jobTeamUserId;
    private Long toUserId;
    private Long totalFilesAdjustment;


    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public Long getJobTeamUserId() {
        return jobTeamUserId;
    }

    public void setJobTeamUserId(Long jobTeamUserId) {
        this.jobTeamUserId = jobTeamUserId;
    }

    public Long getToUserId() {
        return toUserId;
    }

    public void setToUserId(Long toUserId) {
        this.toUserId = toUserId;
    }

    public Long getTotalFilesAdjustment() {
        return totalFilesAdjustment;
    }

    public void setTotalFilesAdjustment(Long totalFilesAdjustment) {
        this.totalFilesAdjustment = totalFilesAdjustment;
    }
}
