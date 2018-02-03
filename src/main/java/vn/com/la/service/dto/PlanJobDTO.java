package vn.com.la.service.dto;

public class PlanJobDTO {
    private Long jobId;
    private String jobName;

    private Long totalFiles = 0L;
    private Long totalToDo = 0L;
    private Long totalToCheck = 0L;
    private Long totalDone = 0L;
    private Long totalDelivery = 0L;

    public void update(Object[] row) {
        if(row[8] != null) {
            totalFiles = Long.parseLong(row[8].toString());
        }
        if(row[9] != null) {
            totalToDo = Long.parseLong(row[9].toString());
        }
        if(row[10] != null) {
            totalToCheck = Long.parseLong(row[10].toString());
        }
        if(row[11] != null) {
            totalDone = Long.parseLong(row[11].toString());
        }
        if(row[12] != null) {
            totalDelivery = Long.parseLong(row[12].toString());
        }
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public Long getTotalToDo() {
        return totalToDo;
    }

    public void setTotalToDo(Long totalToDo) {
        this.totalToDo = totalToDo;
    }

    public Long getTotalToCheck() {
        return totalToCheck;
    }

    public void setTotalToCheck(Long totalToCheck) {
        this.totalToCheck = totalToCheck;
    }

    public Long getTotalDone() {
        return totalDone;
    }

    public void setTotalDone(Long totalDone) {
        this.totalDone = totalDone;
    }

    public Long getTotalDelivery() {
        return totalDelivery;
    }

    public void setTotalDelivery(Long totalDelivery) {
        this.totalDelivery = totalDelivery;
    }

    public Long getTotalFiles() {
        return totalFiles;
    }

    public void setTotalFiles(Long totalFiles) {
        this.totalFiles = totalFiles;
    }
}
