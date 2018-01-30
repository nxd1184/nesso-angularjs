package vn.com.la.web.rest.vm.response;

import vn.com.la.service.dto.ProductionBonusDTO;

import java.util.ArrayList;
import java.util.List;

public class ProductionBonusReportResponseVM extends AbstractResponseVM{
    private List<ProductionBonusDTO> report = new ArrayList<>();

    public List<ProductionBonusDTO> getReport() {
        return report;
    }

    public void setReport(List<ProductionBonusDTO> report) {
        this.report = report;
    }
}
