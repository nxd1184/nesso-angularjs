package vn.com.la.service.dto.param;

import vn.com.la.service.dto.PlanViewEnumDTO;

public class GetAllPlanParamDTO {
    private PlanViewEnumDTO view;
    private String projectCode;
    private String taskCode;


    public PlanViewEnumDTO getView() {
        return view;
    }

    public void setView(PlanViewEnumDTO view) {
        this.view = view;
    }

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
}
