package vn.com.la.web.rest.vm.response;

import vn.com.la.service.dto.QualityDTO;

import java.util.ArrayList;
import java.util.List;

public class QualitiReportResponseVM extends AbstractResponseVM{
    private List<QualityDTO> report = new ArrayList<>();

    public List<QualityDTO> getReport() {
        return report;
    }

    public void setReport(List<QualityDTO> report) {
        this.report = report;
    }
}
