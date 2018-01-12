package vn.com.la.service.dto;


import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import vn.com.la.domain.enumeration.FileStatusEnum;

/**
 * A DTO for the JobTeamUserTask entity.
 */
public class JobTeamUserTaskDTO implements Serializable {

    private Long id;

    @NotNull
    private String originalFileName;

    @NotNull
    private String originalFilePath;

    @NotNull
    private FileStatusEnum status;

    private String fileName;

    @NotNull
    private String filePath;

    private Integer numberOfRework;

    private ZonedDateTime lastCheckInTime;

    private Boolean qcEdit;

    private Boolean rework;

    private Long jobTeamUserId;
    private String jobTeamUserLogin;

    private Long jobId;
    private String jobName;

    private String projectCode;

    private Long qcId;

    private String qcLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getOriginalFilePath() {
        return originalFilePath;
    }

    public void setOriginalFilePath(String originalFilePath) {
        this.originalFilePath = originalFilePath;
    }

    public FileStatusEnum getStatus() {
        return status;
    }

    public void setStatus(FileStatusEnum status) {
        this.status = status;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Integer getNumberOfRework() {
        return numberOfRework;
    }

    public void setNumberOfRework(Integer numberOfRework) {
        this.numberOfRework = numberOfRework;
    }

    public ZonedDateTime getLastCheckInTime() {
        return lastCheckInTime;
    }

    public void setLastCheckInTime(ZonedDateTime lastCheckInTime) {
        this.lastCheckInTime = lastCheckInTime;
    }

    public Boolean isQcEdit() {
        return qcEdit;
    }

    public void setQcEdit(Boolean qcEdit) {
        this.qcEdit = qcEdit;
    }

    public Boolean isRework() {
        return rework;
    }

    public void setRework(Boolean rework) {
        this.rework = rework;
    }

    public Long getJobTeamUserId() {
        return jobTeamUserId;
    }

    public void setJobTeamUserId(Long jobTeamUserId) {
        this.jobTeamUserId = jobTeamUserId;
    }

    public Long getQcId() {
        return qcId;
    }

    public void setQcId(Long userId) {
        this.qcId = userId;
    }

    public String getQcLogin() {
        return qcLogin;
    }

    public void setQcLogin(String userLogin) {
        this.qcLogin = userLogin;
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

    public Boolean getQcEdit() {
        return qcEdit;
    }

    public Boolean getRework() {
        return rework;
    }

    public String getJobTeamUserLogin() {
        return jobTeamUserLogin;
    }

    public void setJobTeamUserLogin(String jobTeamUserLogin) {
        this.jobTeamUserLogin = jobTeamUserLogin;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        JobTeamUserTaskDTO jobTeamUserTaskDTO = (JobTeamUserTaskDTO) o;
        if(jobTeamUserTaskDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), jobTeamUserTaskDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "JobTeamUserTaskDTO{" +
            "id=" + getId() +
            ", originalFileName='" + getOriginalFileName() + "'" +
            ", originalFilePath='" + getOriginalFilePath() + "'" +
            ", status='" + getStatus() + "'" +
            ", fileName='" + getFileName() + "'" +
            ", filePath='" + getFilePath() + "'" +
            ", numberOfRework='" + getNumberOfRework() + "'" +
            ", lastCheckInTime='" + getLastCheckInTime() + "'" +
            ", qcEdit='" + isQcEdit() + "'" +
            ", rework='" + isRework() + "'" +
            "}";
    }
}
