package vn.com.la.web.rest.vm.response;

import vn.com.la.service.dto.NotificationDTO;

import java.util.List;

public class NotifyResponseVM extends AbstractResponseVM {
    private List<NotificationDTO> report;

    public List<NotificationDTO> getReport() {
        return report;
    }

    public void setReport(List<NotificationDTO> report) {
        this.report = report;
    }
}
