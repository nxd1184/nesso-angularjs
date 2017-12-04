package vn.com.la.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import vn.com.la.domain.Task;
import vn.com.la.domain.enumeration.TaskStatusEnum;

/**
 * A DTO for the Task entity.
 */
public class TaskDTO implements Serializable {

    private Long id;

    @NotNull
    private String code;

    @NotNull
    private String name;

    @NotNull
    private Long taskCredit;

    @NotNull
    private TaskStatusEnum status;

    private Long projectId;

    private String projectCode;
    private String projectName;

    public TaskDTO() {
    }

    public TaskDTO(Long id, String code, String name, Long taskCredit, TaskStatusEnum status, Long projectId, String projectCode, String projectName) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.taskCredit = taskCredit;
        this.status = status;
        this.projectId = projectId;
        this.projectCode = projectCode;
        this.projectName = projectName;
    }

    public TaskDTO(Task task) {
        this(task.getId(), task.getCode(), task.getName(), task.getTaskCredit(), task.getStatus(), task.getProject().getId(), task.getProject().getCode(), task.getProject().getName());
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTaskCredit() {
        return taskCredit;
    }

    public void setTaskCredit(Long taskCredit) {
        this.taskCredit = taskCredit;
    }

    public TaskStatusEnum getStatus() {
        return status;
    }

    public void setStatus(TaskStatusEnum status) {
        this.status = status;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TaskDTO taskDTO = (TaskDTO) o;
        if(taskDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), taskDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TaskDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", taskCredit='" + getTaskCredit() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
