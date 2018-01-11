package vn.com.la.service.dto;


import vn.com.la.domain.JobTask;
import vn.com.la.domain.enumeration.JobStatusEnum;

import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Job entity.
 */
public class JobDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private ZonedDateTime deadline;

    private String customerRequirements;

    private Long totalFiles = 0L;

    private Long projectId;

    private String projectCode;

    private String projectName;

    private Set<JobTeamDTO> jobTeams = new HashSet<>();

    private Set<JobTaskDTO> jobTasks = new HashSet<>();

    private JobStatusEnum status;

    private Boolean started;

    private Integer sequenceTask;
    private Integer sequence;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ZonedDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(ZonedDateTime deadline) {
        this.deadline = deadline;
    }

    public String getCustomerRequirements() {
        return customerRequirements;
    }

    public void setCustomerRequirements(String customerRequirements) {
        this.customerRequirements = customerRequirements;
    }

    public Long getTotalFiles() {
        return totalFiles;
    }

    public void setTotalFiles(Long totalFiles) {
        this.totalFiles = totalFiles;
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

    public Set<JobTeamDTO> getJobTeams() {
        return jobTeams;
    }

    public void setJobTeams(Set<JobTeamDTO> jobTeams) {
        this.jobTeams = jobTeams;
    }

    public Set<JobTaskDTO> getJobTasks() {
        return jobTasks;
    }

    public void setJobTasks(Set<JobTaskDTO> jobTasks) {
        this.jobTasks = jobTasks;
    }

    public JobStatusEnum getStatus() {
        return status;
    }

    public void setStatus(JobStatusEnum status) {
        this.status = status;
    }

    public Boolean getStarted() {
        return started;
    }

    public void setStarted(Boolean started) {
        this.started = started;
    }

    public Integer getSequenceTask() {
        return sequenceTask;
    }

    public void setSequenceTask(Integer sequenceTask) {
        this.sequenceTask = sequenceTask;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
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

        JobDTO jobDTO = (JobDTO) o;
        if(jobDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), jobDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "JobDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", deadline='" + getDeadline() + "'" +
            ", customerRequirements='" + getCustomerRequirements() + "'" +
            ", totalFiles='" + getTotalFiles() + "'" +
            "}";
    }
}
