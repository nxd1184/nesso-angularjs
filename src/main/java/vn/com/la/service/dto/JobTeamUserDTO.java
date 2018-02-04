package vn.com.la.service.dto;


import vn.com.la.domain.JobTeamUserTask;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the JobTeamUser entity.
 */
public class JobTeamUserDTO implements Serializable {

    private Long id;

    @NotNull
    private Long totalFiles;

    private Long jobTeamId;

    private Long userId;

    private String name;

    private String userLogin;

    private Long capacity;

    private Long totalToDoFiles = 0L;
    private Long totalToCheckFiles = 0L;
    private Long totalDoneFiles = 0L;
    private Long totalDeliveryFiles = 0L;

    private Set<JobTeamUserTaskDTO> jobTeamUserTasks = new HashSet<>();

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

    public Long getJobTeamId() {
        return jobTeamId;
    }

    public void setJobTeamId(Long jobTeamId) {
        this.jobTeamId = jobTeamId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCapacity() {
        return capacity;
    }

    public void setCapacity(Long capacity) {
        this.capacity = capacity;
    }

    public Set<JobTeamUserTaskDTO> getJobTeamUserTasks() {
        return jobTeamUserTasks;
    }

    public void setJobTeamUserTasks(Set<JobTeamUserTaskDTO> jobTeamUserTasks) {
        this.jobTeamUserTasks = jobTeamUserTasks;
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

    public void addJobTeamUserTask(JobTeamUserTaskDTO jobTeamUserTaskDTO) {
        this.jobTeamUserTasks.add(jobTeamUserTaskDTO);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        JobTeamUserDTO jobTeamUserDTO = (JobTeamUserDTO) o;
        if(jobTeamUserDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), jobTeamUserDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "JobTeamUserDTO{" +
            "id=" + getId() +
            ", totalFiles='" + getTotalFiles() + "'" +
            "}";
    }
}
