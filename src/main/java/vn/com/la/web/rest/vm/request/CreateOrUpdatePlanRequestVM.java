package vn.com.la.web.rest.vm.request;

import org.hibernate.validator.constraints.NotBlank;
import vn.com.la.domain.JobTask;
import vn.com.la.domain.JobTeam;
import vn.com.la.domain.enumeration.JobTypeEnum;
import vn.com.la.service.dto.JobDTO;
import vn.com.la.service.dto.JobTaskDTO;
import vn.com.la.service.dto.JobTeamDTO;
import vn.com.la.service.dto.TaskDTO;

import java.time.Instant;
import java.util.List;
import java.util.Set;


public class CreateOrUpdatePlanRequestVM extends AbstractRequestVM{

    private Long jobId;

    @NotBlank
    private String deadline;

    private Set<JobTaskDTO> tasks;

    private Set<JobTeamDTO> teams;

    private String customerRequirements;

    private Integer sequenceTask;

    private Long totalFiles;

    private JobTypeEnum type;

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
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

    public JobTypeEnum getType() {
        return type;
    }

    public void setType(JobTypeEnum type) {
        this.type = type;
    }
}
