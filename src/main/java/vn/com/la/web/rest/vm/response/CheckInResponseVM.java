package vn.com.la.web.rest.vm.response;

import vn.com.la.service.dto.CheckInReport;

import java.util.ArrayList;
import java.util.List;

public class CheckInResponseVM extends AbstractResponseVM{
    private List<CheckInReport> report = new ArrayList<>();

    public List<CheckInReport> getReport() {
        return report;
    }

    public void setReport(List<CheckInReport> report) {
        this.report = report;
    }
}
