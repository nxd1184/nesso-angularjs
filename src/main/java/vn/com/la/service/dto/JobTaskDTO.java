package vn.com.la.service.dto;

import java.io.Serializable;
import java.util.Objects;

public class JobTaskDTO implements Serializable {

    private Long id;

    private Long taskId;

    private Long jobId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        JobTaskDTO jobTaskDTO = (JobTaskDTO) o;
        if(jobTaskDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), jobTaskDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "JobTaskDTO{" +
            "id=" + getId() +
            ", taskId='" + getTaskId() + "'" +
            ", jobId='" + getJobId() + "'" +
            "}";
    }
}
