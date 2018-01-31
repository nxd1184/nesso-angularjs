package vn.com.la.web.rest.vm.response;

import vn.com.la.service.dto.QualitiDTO;

import java.util.ArrayList;
import java.util.List;

public class QualitiReportResponseVM extends AbstractResponseVM{
    private List<QualitiDTO> report = new ArrayList<>();

    public List<QualitiDTO> getReport() {
        return report;
    }

    public void setReport(List<QualitiDTO> report) {
        this.report = report;
    }
}
