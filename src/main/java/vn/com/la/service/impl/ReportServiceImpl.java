package vn.com.la.service.impl;

import vn.com.la.config.Constants;
import vn.com.la.domain.enumeration.FileStatusEnum;
import vn.com.la.service.JobService;
import vn.com.la.service.JobTeamUserTaskService;
import vn.com.la.service.ReportService;
import vn.com.la.service.dto.param.DashboardReportParam;
import vn.com.la.service.util.LADateTimeUtil;
import vn.com.la.web.rest.vm.response.DashboardResponseVM;

public class ReportServiceImpl implements ReportService{

    private final JobService jobService;
    private final JobTeamUserTaskService jobTeamUserTaskService;

    public ReportServiceImpl(JobService jobService, JobTeamUserTaskService jobTeamUserTaskService) {
        this.jobService = jobService;
        this.jobTeamUserTaskService = jobTeamUserTaskService;
    }

    @Override
    public DashboardResponseVM getDashboardData(DashboardReportParam param) {
        DashboardResponseVM rs = new DashboardResponseVM();

        rs.setTotalReceive(jobService.getTotalReceiverByDateTime(param.getFromDate(), param.getToDate()));
        rs.setTotalToDo(jobTeamUserTaskService.countByStatusAndDateRange(Constants.TO_DO_STATUS_LIST, LADateTimeUtil.zonedDateTimeToInstant(param.getFromDate()), LADateTimeUtil.zonedDateTimeToInstant(param.getToDate())));
        rs.setTotalToCheck(jobTeamUserTaskService.countByStatusAndDateRange(Constants.TO_CHECK_STATUS_LIST, LADateTimeUtil.zonedDateTimeToInstant(param.getFromDate()), LADateTimeUtil.zonedDateTimeToInstant(param.getToDate())));
        rs.setTotalDone(jobTeamUserTaskService.countByStatusAndDateRange(Constants.DONE_STATUS_LIST, LADateTimeUtil.zonedDateTimeToInstant(param.getFromDate()), LADateTimeUtil.zonedDateTimeToInstant(param.getToDate())));

        rs.setUrgentJobs(jobService.findByDeadlineBetween(param.getFromDate(), param.getToDate()));

        return null;
    }
}
