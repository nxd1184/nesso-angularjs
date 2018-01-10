package vn.com.la.service.dto.param;

import vn.com.la.domain.JobTask;
import vn.com.la.domain.JobTeam;
import vn.com.la.service.dto.JobTaskDTO;
import vn.com.la.service.dto.JobTeamDTO;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

public class UpdatePlanParamDTO {

    Long jobId;

    private ZonedDateTime deadline;

    private Set<JobTaskDTO> tasks;

    private Set<JobTeamDTO> teams;

    private String customerRequirements;

    private Integer sequenceTask;

    private Long totalFiles;

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public ZonedDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(ZonedDateTime deadline) {
        this.deadline = deadline;
    }

    public Set<JobTaskDTO> getTasks() {
        return tasks;
    }

    public void setTasks(Set<JobTaskDTO> tasks) {
        this.tasks = tasks;
    }

    public Set<JobTeamDTO> getTeams() {
        return teams;
    }

    public void setTeams(Set<JobTeamDTO> teams) {
        this.teams = teams;
    }

    public String getCustomerRequirements() {
        return customerRequirements;
    }

    public void setCustomerRequirements(String customerRequirements) {
        this.customerRequirements = customerRequirements;
    }

    public Integer getSequenceTask() {
        return sequenceTask;
    }

    public void setSequenceTask(Integer sequenceTask) {
        this.sequenceTask = sequenceTask;
    }

    public Long getTotalFiles() {
        return totalFiles;
    }

    public void setTotalFiles(Long totalFiles) {
        this.totalFiles = totalFiles;
    }
}
