package vn.com.la.service;

import org.joda.time.DateTime;
import vn.com.la.service.dto.param.DashboardReportParam;
import vn.com.la.web.rest.vm.response.DashboardResponseVM;
import vn.com.la.web.rest.vm.response.ProductionBonusReportResponseVM;

import java.util.Date;

public interface ReportService {
    DashboardResponseVM getDashboardData(DashboardReportParam param);
    DashboardResponseVM getDashboardDataForThisMonth();

    ProductionBonusReportResponseVM getProductBonusReport(DateTime fromDate, DateTime toDate);
}
