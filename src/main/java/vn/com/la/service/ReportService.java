package vn.com.la.service;

import org.joda.time.DateTime;

import vn.com.la.service.dto.param.DashboardReportParam;
import vn.com.la.web.rest.vm.response.*;

import java.util.Date;

public interface ReportService {
    DashboardResponseVM getDashboardData(DashboardReportParam param);
    DashboardResponseVM getDashboardDataForThisMonth();

    ProductionBonusReportResponseVM getProductBonusReport(DateTime fromDate, DateTime toDate);
    QualitiReportResponseVM getQualityReport(DateTime fromDate, DateTime toDate);

    DeliveryQualityResponseVM getDeliveryQualityReportForUser( DateTime fromDate, DateTime toDate);
    DeliveryQualityResponseVM getDeliveryQualityReportForFreelancer( DateTime fromDate, DateTime toDate);
    CheckInResponseVM getCheckinReport( DateTime fromDate, DateTime toDate);
    public ProjectMemberReportResponseVM getProjectMemberReport(DateTime fromDate, DateTime toDate);
}
