package vn.com.la.web.rest.vm.response;

import vn.com.la.service.dto.ProjectMemberDTO;

import java.util.ArrayList;
import java.util.List;

public class ProjectMemberReportResponseVM extends AbstractResponseVM{
    private ProjectMemberDTO report;

    public ProjectMemberDTO getReport() {
        return report;
    }

    public void setReport(ProjectMemberDTO report) {
        this.report = report;
    }
}

