package vn.com.la.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.la.service.JobService;
import vn.com.la.service.ReportService;
import vn.com.la.service.dto.param.DashboardReportParam;
import vn.com.la.service.util.LACommonUtil;
import vn.com.la.service.util.LADateTimeUtil;
import vn.com.la.service.util.LAStringUtil;
import vn.com.la.web.rest.util.HeaderUtil;
import vn.com.la.web.rest.vm.request.DashboardRequestVM;
import vn.com.la.web.rest.vm.response.DashboardResponseVM;

import javax.validation.Valid;
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
}
