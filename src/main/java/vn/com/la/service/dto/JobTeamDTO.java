package vn.com.la.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the JobTeam entity.
 */
public class JobTeamDTO implements Serializable {

    private Long id;

    @NotNull
    private Long totalFiles;

    private Long jobId;

    private String jobName;

    private Long teamId;

    private String teamName;

    private Long projectId;

    private String projectName;

    private Set<JobTeamUserDTO> jobTeamUsers = new HashSet<>();

    private Long totalToDoFiles;
    private Long totalToCheckFiles;
    private Long totalDoneFiles;
    private Long totalDeliveryFiles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTotalFiles() {
        return totalFiles;
    }

    public void setTotalFiles(Long totalFiles) {
        this.totalFiles = totalFiles;
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

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Set<JobTeamUserDTO> getJobTeamUsers() {
        return jobTeamUsers;
    }

    public void setJobTeamUsers(Set<JobTeamUserDTO> jobTeamUsers) {
        this.jobTeamUsers = jobTeamUsers;
    }

    public Long getTotalToDoFiles() {
        return totalToDoFiles;
    }

    public void setTotalToDoFiles(Long totalToDoFiles) {
        this.totalToDoFiles = totalToDoFiles;
    }

    public Long getTotalToCheckFiles() {
        return totalToCheckFiles;
    }

    public void setTotalToCheckFiles(Long totalToCheckFiles) {
        this.totalToCheckFiles = totalToCheckFiles;
    }

    public Long getTotalDoneFiles() {
        return totalDoneFiles;
    }

    public void setTotalDoneFiles(Long totalDoneFiles) {
        this.totalDoneFiles = totalDoneFiles;
    }

    public Long getTotalDeliveryFiles() {
        return totalDeliveryFiles;
    }

    public void setTotalDeliveryFiles(Long totalDeliveryFiles) {
        this.totalDeliveryFiles = totalDeliveryFiles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        JobTeamDTO jobTeamDTO = (JobTeamDTO) o;
        if(jobTeamDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), jobTeamDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "JobTeamDTO{" +
            "id=" + getId() +
            ", totalFiles='" + getTotalFiles() + "'" +
            "}";
    }
}
