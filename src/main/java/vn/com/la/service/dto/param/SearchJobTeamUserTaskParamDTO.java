package vn.com.la.service.dto.param;

import vn.com.la.domain.enumeration.FileStatusEnum;

import java.util.List;

public class SearchJobTeamUserTaskParamDTO {
    private List<FileStatusEnum> statusList;
    private String assignee;

    public List<FileStatusEnum> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<FileStatusEnum> statusList) {
        this.statusList = statusList;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }
}
