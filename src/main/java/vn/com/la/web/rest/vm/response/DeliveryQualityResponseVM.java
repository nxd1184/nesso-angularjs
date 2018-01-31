package vn.com.la.web.rest.vm.response;

import vn.com.la.service.dto.DeliveryQualityReportDTO;

import java.util.ArrayList;
import java.util.List;

public class DeliveryQualityResponseVM extends AbstractResponseVM{
    private List<DeliveryQualityReportDTO> report = new ArrayList<>();

    public List<DeliveryQualityReportDTO> getReport() {
        return report;
    }

    public void setReport(List<DeliveryQualityReportDTO> report) {
        this.report = report;
    }
}
