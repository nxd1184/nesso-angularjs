package vn.com.la.web.rest.vm.request;

import org.hibernate.validator.constraints.NotEmpty;


public class SyncUpProjectRequestVM extends AbstractRequestVM{

    @NotEmpty
    private String projectCode;

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }
}
