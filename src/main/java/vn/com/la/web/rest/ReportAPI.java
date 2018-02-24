package vn.com.la.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.com.la.service.ReportService;
import vn.com.la.service.dto.ReportDTO;
import vn.com.la.service.dto.param.DashboardReportParam;
import vn.com.la.service.util.LADateTimeUtil;
import vn.com.la.web.rest.vm.response.*;

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

    @GetMapping("/report/delivery-quality-report")
    @Timed
    public ResponseEntity<DeliveryQualityResponseVM> getDeliveryQualityReport(@ApiParam(required = true) String fromDate,
                                                                      @ApiParam(required = true) String toDate) {
        log.debug("Request to get quality delivery report");

        DateTime fromDateZDT = LADateTimeUtil.isoStringToDateTime(fromDate);
        DateTime toDateZDT = LADateTimeUtil.isoStringToDateTime(toDate);

        DeliveryQualityResponseVM rs = reportService.getDeliveryQualityReportForUser(fromDateZDT, toDateZDT);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(rs));
    }

    @GetMapping("/report/freelancer-report")
    @Timed
    public ResponseEntity<DeliveryQualityResponseVM> getFreelancerReport(@ApiParam(required = true) String fromDate,
                                                                              @ApiParam(required = true) String toDate) {
        log.debug("Request to get quality freelancer report");

        DateTime fromDateZDT = LADateTimeUtil.isoStringToDateTime(fromDate);
        DateTime toDateZDT = LADateTimeUtil.isoStringToDateTime(toDate);

        DeliveryQualityResponseVM rs = reportService.getDeliveryQualityReportForFreelancer(fromDateZDT, toDateZDT);

        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(rs));
    }

    @GetMapping("/report/checkin")
    @Timed
    public ResponseEntity<CheckInResponseVM> getCheckin(@ApiParam(required = true) String fromDate,
                                                                         @ApiParam(required = true) String toDate) {
        log.debug("Request to get quality checkin");

        DateTime fromDateZDT = LADateTimeUtil.isoStringToDateTime(fromDate);
        DateTime toDateZDT = LADateTimeUtil.isoStringToDateTime(toDate);

        CheckInResponseVM rs = reportService.getCheckinReport(fromDateZDT, toDateZDT);

        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(rs));
    }

    @GetMapping("/report/project-member")
    @Timed
    public ResponseEntity<ProjectMemberReportResponseVM> getProjectMemberReport(@ApiParam(required = true) String fromDate,
                                                        @ApiParam(required = true) String toDate) {
        log.debug("Request to get quality checkin");

        DateTime fromDateZDT = LADateTimeUtil.isoStringToDateTime(fromDate);
        DateTime toDateZDT = LADateTimeUtil.isoStringToDateTime(toDate);

        ProjectMemberReportResponseVM rs = reportService.getProjectMemberReport(fromDateZDT, toDateZDT);

        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(rs));
    }
    @PostMapping("/report/export-xls")
    @Timed
    public ResponseEntity<byte[]> exportReportToXls(@ApiParam(required = true) @Valid @RequestBody ReportDTO reportDTO) {
        log.debug("Request to export the report into xls file");
        ReportXlsResponseVM rs = reportService.exportReportToXls(reportDTO);
        ResponseEntity responseEntity = null;
        if (rs.isSuccess()) {
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            responseHeaders.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            responseHeaders.add("content-disposition", "attachment; filename=" + reportDTO.getName());
            responseHeaders.add("x-filename",reportDTO.getName());
            responseEntity = new ResponseEntity(rs.getBytes(), responseHeaders, HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity ("File Not Found", HttpStatus.OK);
        }
        return responseEntity;
    }
}
