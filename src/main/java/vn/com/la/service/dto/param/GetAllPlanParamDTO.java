package vn.com.la.service.dto.param;

import vn.com.la.service.dto.PlanTypeEnumDTO;
import vn.com.la.service.dto.PlanViewEnumDTO;

import java.time.ZonedDateTime;

public class GetAllPlanParamDTO {
    private PlanViewEnumDTO view;
    private PlanTypeEnumDTO type;
    private String projectCode;
    private String taskCode;
    private ZonedDateTime fromDate;
    private ZonedDateTime toDate;

    public PlanTypeEnumDTO getType() {
        return type;
    }

    public void setType(PlanTypeEnumDTO type) {
        this.type = type;
    }

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

    public ZonedDateTime getFromDate() {
        return fromDate;
    }

    public void setFromDate(ZonedDateTime fromDate) {
        this.fromDate = fromDate;
    }

    public ZonedDateTime getToDate() {
        return toDate;
    }

    public void setToDate(ZonedDateTime toDate) {
        this.toDate = toDate;
    }
}
