package vn.com.la.service.dto.param;

import vn.com.la.domain.enumeration.ProjectStatusEnum;

public class SearchProjectParamDTO {
    private String projectCode;
    private String taskCode;
    private ProjectStatusEnum status;

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getTaskCode() {
        return taskCode;
    }

    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode;
    }

    public ProjectStatusEnum getStatus() {
        return status;
    }

    public void setStatus(ProjectStatusEnum status) {
        this.status = status;
    }
}
