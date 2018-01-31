package vn.com.la.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.la.service.ReportService;
import vn.com.la.service.dto.param.DashboardReportParam;
import vn.com.la.service.util.LADateTimeUtil;
import vn.com.la.web.rest.vm.response.DashboardResponseVM;
import vn.com.la.web.rest.vm.response.ProductionBonusReportResponseVM;
import vn.com.la.web.rest.vm.response.QualitiReportResponseVM;

import java.time.ZonedDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ReportAPI {

    private final Logger log = LoggerFactory.getLogger(ReportAPI.class);

    private final ReportService reportService;

    public ReportAPI(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/report/dashboard/this-week")
    @Timed
    public ResponseEntity<DashboardResponseVM> getProductivityForDashboardThisWeek(@ApiParam(required = true) String fromDate,
                                                                                   @ApiParam(required = true) String toDate,
                                                                                   @ApiParam(required = true) String fromDeadlineDate,
                                                                                   @ApiParam(required = true) String toDeadlineDate) {

        log.debug("Request to get productivity for dashboard");

        ZonedDateTime fromDateZDT = LADateTimeUtil.isoStringToZonedDateTime(fromDate);
        ZonedDateTime toDateZDT = LADateTimeUtil.isoStringToZonedDateTime(toDate);
        ZonedDateTime fromDeadlineZDT = LADateTimeUtil.isoStringToZonedDateTime(fromDeadlineDate);
        ZonedDateTime toDeadlineZDT = LADateTimeUtil.isoStringToZonedDateTime(toDeadlineDate);

        DashboardReportParam param = new DashboardReportParam();
        param.setFromDate(fromDateZDT);
        param.setToDate(toDateZDT);
        param.setFromDealineDate(fromDeadlineZDT);
        param.setToDealineDate(toDeadlineZDT);

        DashboardResponseVM rs = reportService.getDashboardData(param);

        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(rs));
    }

    @GetMapping("/report/dashboard/this-month")
    @Timed
    public ResponseEntity<DashboardResponseVM> getProductivityForDashboardThisMonth() {
        log.debug("Request to get productivity for dashboard");
        DashboardResponseVM rs = reportService.getDashboardDataForThisMonth();
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(rs));
    }

    @GetMapping("/report/production-bonus")
    @Timed
    public ResponseEntity<ProductionBonusReportResponseVM> getProductionBonusReport(@ApiParam(required = true) String fromDate,
                                                                                    @ApiParam(required = true) String toDate) {
        log.debug("Request to get production bonus report");

        DateTime fromDateZDT = LADateTimeUtil.isoStringToDateTime(fromDate);
        DateTime toDateZDT = LADateTimeUtil.isoStringToDateTime(toDate);

        ProductionBonusReportResponseVM rs = reportService.getProductBonusReport(fromDateZDT, toDateZDT);

        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(rs));
    }

    @GetMapping("/report/quality-report")
    @Timed
    public ResponseEntity<QualitiReportResponseVM> getQualityReport(@ApiParam(required = true) String fromDate,
                                                                                    @ApiParam(required = true) String toDate) {
        log.debug("Request to get quality report");

        DateTime fromDateZDT = LADateTimeUtil.isoStringToDateTime(fromDate);
        DateTime toDateZDT = LADateTimeUtil.isoStringToDateTime(toDate);

        QualitiReportResponseVM rs = reportService.getQualityReport(fromDateZDT, toDateZDT);

        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(rs));
    }
}
