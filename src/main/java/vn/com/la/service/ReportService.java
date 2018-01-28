package vn.com.la.service;

import vn.com.la.service.dto.param.DashboardReportParam;
import vn.com.la.web.rest.vm.response.DashboardResponseVM;

public interface ReportService {
    DashboardResponseVM getDashboardData(DashboardReportParam param);
}
