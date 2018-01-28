package vn.com.la.service.impl;

import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import vn.com.la.config.Constants;
import vn.com.la.domain.enumeration.FileStatusEnum;
import vn.com.la.service.JobService;
import vn.com.la.service.JobTeamUserTaskService;
import vn.com.la.service.ReportService;
import vn.com.la.service.dto.UserProductivityDTO;
import vn.com.la.service.dto.param.DashboardReportParam;
import vn.com.la.service.util.LADateTimeUtil;
import vn.com.la.web.rest.vm.response.DashboardResponseVM;


import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService{

    private final JobService jobService;
    private final JobTeamUserTaskService jobTeamUserTaskService;

    private final EntityManager entityManager;

    public ReportServiceImpl(JobService jobService, JobTeamUserTaskService jobTeamUserTaskService,
                             EntityManager entityManager) {
        this.jobService = jobService;
        this.jobTeamUserTaskService = jobTeamUserTaskService;
        this.entityManager = entityManager;
    }

    @Override
    public DashboardResponseVM getDashboardData(DashboardReportParam param) {
        DashboardResponseVM rs = new DashboardResponseVM();

        rs.setTotalReceive(jobService.getTotalReceiverByDateTime(param.getFromDate(), param.getToDate()));
        rs.setTotalToDo(jobTeamUserTaskService.countByStatusAndDateRange(Constants.TO_DO_STATUS_LIST, LADateTimeUtil.zonedDateTimeToInstant(param.getFromDate()), LADateTimeUtil.zonedDateTimeToInstant(param.getToDate())));
        rs.setTotalToCheck(jobTeamUserTaskService.countByStatusAndDateRange(Constants.TO_CHECK_STATUS_LIST, LADateTimeUtil.zonedDateTimeToInstant(param.getFromDate()), LADateTimeUtil.zonedDateTimeToInstant(param.getToDate())));
        rs.setTotalDone(jobTeamUserTaskService.countByStatusAndDateRange(Constants.DONE_STATUS_LIST, LADateTimeUtil.zonedDateTimeToInstant(param.getFromDate()), LADateTimeUtil.zonedDateTimeToInstant(param.getToDate())));

        rs.setUrgentJobs(jobService.findByDeadlineBetween(param.getFromDealineDate(), param.getToDealineDate()));

        return rs;
    }

    @Override
    public DashboardResponseVM getDashboardDataForThisMonth() {
        StringBuilder sqlBuilder = new StringBuilder();
        DashboardResponseVM rs = new DashboardResponseVM();

        DateTime now = DateTime.now();
        DateTime startDateOfMonth = now.dayOfMonth().withMinimumValue();
        DateTime endDateOfMonth = now.dayOfMonth().withMaximumValue();

        sqlBuilder.append("SELECT sum(task.task_credit) FROM nesso.job_team_user_task jtut ");
        sqlBuilder.append(" inner join job_team_user jtu on jtut.job_team_user_id = jtu.id");
        sqlBuilder.append(" inner join job_team jt on jtu.job_team_id = jt.id");
        sqlBuilder.append(" inner join job j on jt.job_id = j.id");
        sqlBuilder.append(" inner join job_task job_task on job_task.job_id = j.id");
        sqlBuilder.append(" inner join task task on job_task.task_id = task.id");
        sqlBuilder.append(" where jtut.status = 'DONE' AND jtut.last_done_time BETWEEN ? AND ?");

        Query query = entityManager.createNativeQuery(sqlBuilder.toString());

        // for this month
        try {
            query.setParameter(1, startDateOfMonth.toString(LADateTimeUtil.DATETIME_ISO_FORMAT));
            query.setParameter(2, endDateOfMonth.toString(LADateTimeUtil.DATETIME_ISO_FORMAT));
            Object singleResult = query.getSingleResult();
            rs.setCountTotalDoneForThisMonth(Long.parseLong(singleResult.toString()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // for last month
        try {
            query.setParameter(1, startDateOfMonth.minusMonths(1).toString(LADateTimeUtil.DATETIME_ISO_FORMAT));
            query.setParameter(2, endDateOfMonth.minusMonths(1).toString(LADateTimeUtil.DATETIME_ISO_FORMAT));
            Object singleResult = query.getSingleResult();
            rs.setCountTotalDoneForLastMonth(Long.parseLong(singleResult.toString()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // for best month
        sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT MONTH(jtut.last_done_time), sum(task.task_credit) as total_credit FROM nesso.job_team_user_task jtut ");
        sqlBuilder.append(" inner join job_team_user jtu on jtut.job_team_user_id = jtu.id");
        sqlBuilder.append(" inner join job_team jt on jtu.job_team_id = jt.id");
        sqlBuilder.append(" inner join job j on jt.job_id = j.id");
        sqlBuilder.append(" inner join job_task job_task on job_task.job_id = j.id");
        sqlBuilder.append(" inner join task task on job_task.task_id = task.id");
        sqlBuilder.append(" where jtut.status = 'DONE'");
        sqlBuilder.append(" GROUP BY MONTH(jtut.last_done_time)");
        sqlBuilder.append(" ORDER BY total_credit");
        try {
            query = entityManager.createNativeQuery(sqlBuilder.toString());
            List<Object[]> rows = query.getResultList();
            rs.setCountTotalDoneForBestMonth(Long.parseLong(rows.get(0)[1].toString()));
        }catch (Exception ex) {
            ex.printStackTrace();
        }

        // productivity of users
        sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT ju.last_name, sum(task.task_credit) as total_credit FROM nesso.job_team_user_task jtut ");
        sqlBuilder.append(" inner join job_team_user jtu on jtut.job_team_user_id = jtu.id");
        sqlBuilder.append(" inner join job_team jt on jtu.job_team_id = jt.id");
        sqlBuilder.append(" inner join job j on jt.job_id = j.id");
        sqlBuilder.append(" inner join job_task job_task on job_task.job_id = j.id");
        sqlBuilder.append(" inner join task task on job_task.task_id = task.id");
        sqlBuilder.append(" where jtut.status = 'DONE' AND jtut.last_done_time BETWEEN ? AND ?");
        sqlBuilder.append(" GROUP BY ju.last_name");
        sqlBuilder.append(" ORDER BY total_credit");
        sqlBuilder.append(" LIMIT 6");
        try {
            query = entityManager.createNativeQuery(sqlBuilder.toString());
            query.setParameter(1, startDateOfMonth.toString(LADateTimeUtil.DATETIME_ISO_FORMAT));
            query.setParameter(2, endDateOfMonth.toString(LADateTimeUtil.DATETIME_ISO_FORMAT));
            List<Object[]> rows = query.getResultList();
            List<UserProductivityDTO> userProductivityDTOS = new ArrayList<>();
            for(Object[] row: rows) {
                UserProductivityDTO userProductivityDTO = new UserProductivityDTO();
                if(row[0] != null) {
                    userProductivityDTO.setName(row[0].toString());
                }
                if(row[1] != null) {
                    userProductivityDTO.setTotalCredit(Long.parseLong(row[1].toString()));
                }

            }

        }catch (Exception ex) {
            ex.printStackTrace();
        }


        return rs;
    }
}
