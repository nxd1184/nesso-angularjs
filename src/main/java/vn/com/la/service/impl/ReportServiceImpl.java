package vn.com.la.service.impl;

import org.apache.commons.lang3.RandomUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import vn.com.la.config.Constants;
import vn.com.la.service.JobService;
import vn.com.la.service.JobTeamUserTaskService;
import vn.com.la.service.ReportService;
import vn.com.la.service.dto.*;
import vn.com.la.service.dto.param.DashboardReportParam;
import vn.com.la.service.util.LADateTimeUtil;
import vn.com.la.web.rest.vm.response.*;


import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

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
        DateTime startDateOfMonth = LADateTimeUtil.toTimeAtStartOfDay(now.dayOfMonth().withMinimumValue());
        DateTime endDateOfMonth = LADateTimeUtil.toTimeAtEndOfDay(now.dayOfMonth().withMaximumValue());

        sqlBuilder.append("SELECT sum(task.task_credit) FROM job_team_user_task jtut ");
        sqlBuilder.append(" inner join job_team_user jtu on jtut.job_team_user_id = jtu.id");
        sqlBuilder.append(" inner join job_team jt on jtu.job_team_id = jt.id");
        sqlBuilder.append(" inner join job j on jt.job_id = j.id");
        sqlBuilder.append(" inner join job_task job_task on job_task.job_id = j.id");
        sqlBuilder.append(" inner join task task on job_task.task_id = task.id");
        sqlBuilder.append(" where jtut.status = 'DONE' AND jtut.last_done_time BETWEEN ? AND ?");

        Query query = entityManager.createNativeQuery(sqlBuilder.toString());

        // for this month
        try {
            query.setParameter(1, startDateOfMonth.toString(LADateTimeUtil.DATETIME_FORMAT));
            query.setParameter(2, endDateOfMonth.toString(LADateTimeUtil.DATETIME_FORMAT));
            Object singleResult = query.getSingleResult();
            if (singleResult != null) {
                rs.setCountTotalDoneForThisMonth(Long.parseLong(singleResult.toString()));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // for last month
        try {
            query.setParameter(1, startDateOfMonth.minusMonths(1).toString(LADateTimeUtil.DATETIME_FORMAT));
            query.setParameter(2, endDateOfMonth.minusMonths(1).toString(LADateTimeUtil.DATETIME_FORMAT));
            Object singleResult = query.getSingleResult();
            if (singleResult != null) {
                rs.setCountTotalDoneForLastMonth(Long.parseLong(singleResult.toString()));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // for best month
        sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT MONTH(jtut.last_done_time), sum(task.task_credit) as total_credit FROM job_team_user_task jtut ");
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
            if (rows != null && rows.size() > 0) {
                rs.setCountTotalDoneForBestMonth(Long.parseLong(rows.get(0)[1].toString()));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // productivity of users
        sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT ju.last_name, sum(task.task_credit) as total_credit FROM job_team_user_task jtut ");
        sqlBuilder.append(" inner join job_team_user jtu on jtut.job_team_user_id = jtu.id");
        sqlBuilder.append(" inner join jhi_user ju on jtu.user_id = ju.id");
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
            query.setParameter(1, startDateOfMonth.toString(LADateTimeUtil.DATETIME_FORMAT));
            query.setParameter(2, endDateOfMonth.toString(LADateTimeUtil.DATETIME_FORMAT));
            List<Object[]> rows = query.getResultList();
            List<UserProductivityDTO> userProductivityDTOS = new ArrayList<>();
            for (Object[] row : rows) {
                UserProductivityDTO userProductivityDTO = new UserProductivityDTO();
                if (row[0] != null) {
                    userProductivityDTO.setName(row[0].toString());
                }
                if (row[1] != null) {
                    userProductivityDTO.setTotalCredit(Long.parseLong(row[1].toString()));
                }

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }


        return rs;
    }

    @Override
    public ProductionBonusReportResponseVM getProductBonusReport(DateTime fromDate, DateTime toDate) {
        StringBuilder sqlBuilder = new StringBuilder();

        sqlBuilder.append("SELECT ju.id, ju.last_name, p.id as project_id, p.name as project_name, j.id as job_id, j.name as job_name, count(jtut.id) as volumn, sum(task.task_credit) as credit, count(jtut.id) * sum(task.task_credit) as total_credit");
        sqlBuilder.append(" FROM job_team_user_task jtut");
        sqlBuilder.append(" inner join job_team_user jtu on jtut.job_team_user_id = jtu.id");
        sqlBuilder.append(" inner join jhi_user ju on jtu.user_id = ju.id");
        sqlBuilder.append(" inner join job_team jt on jtu.job_team_id = jt.id");
        sqlBuilder.append(" inner join job j on jt.job_id = j.id");
        sqlBuilder.append(" inner join job_task job_task on job_task.job_id = j.id");
        sqlBuilder.append(" inner join project p on p.id = j.project_id");
        sqlBuilder.append(" inner join task task on job_task.task_id = task.id");
        sqlBuilder.append(" where jtut.status = 'DONE' AND last_done_time between ? and ?");
        sqlBuilder.append(" group by ju.id, ju.last_name, p.id, p.name, j.id, j.name;");

        Query query = entityManager.createNativeQuery(sqlBuilder.toString());
        query.setParameter(1, fromDate.toString(LADateTimeUtil.DATETIME_FORMAT));
        query.setParameter(2, toDate.toString(LADateTimeUtil.DATETIME_FORMAT));
        List<Object[]> rows = query.getResultList();

        List<ProductionBonusDTO> report = new ArrayList<>();
        for (Object[] row : rows) {
            ProductionBonusDTO productionBonusDTO = new ProductionBonusDTO();
            productionBonusDTO.setUserId(Long.parseLong(row[0].toString()));
            productionBonusDTO.setEmployee(row[1].toString());
            productionBonusDTO.setProjectId(Long.parseLong(row[2].toString()));
            productionBonusDTO.setProjectName(row[3].toString());
            productionBonusDTO.setJobId(Long.parseLong(row[4].toString()));
            productionBonusDTO.setJobName(row[5].toString());
            productionBonusDTO.setVolumn(Long.parseLong(row[6].toString()));
            productionBonusDTO.setCredit(Long.parseLong(row[7].toString()));
            productionBonusDTO.setTotalCredit(Long.parseLong(row[8].toString()));

            report.add(productionBonusDTO);
        }

        //TODO: Fake data:
        for (int i = 0; i < 5; i++) {
            ProductionBonusDTO productionBonusDTO = new ProductionBonusDTO();
            productionBonusDTO.setUserId(new Long(i));
            productionBonusDTO.setProjectId(new Long(i));
            productionBonusDTO.setJobId(new Long(i));
            productionBonusDTO.setEmployee("User 0" + i);
            productionBonusDTO.setProjectName("ProjectName 0" + i);
            productionBonusDTO.setJobName("Job 0" + i);
            productionBonusDTO.setVolumn(RandomUtils.nextLong(100, 2000));
            productionBonusDTO.setCredit(RandomUtils.nextLong(100, 2000));
            productionBonusDTO.setTotalCredit(productionBonusDTO.getCredit() * productionBonusDTO.getVolumn());
            report.add(productionBonusDTO);
        }

        ProductionBonusReportResponseVM rs = new ProductionBonusReportResponseVM();

        rs.setReport(report);
        return rs;
    }

    @Override
    public QualitiReportResponseVM getQualityReport(DateTime fromDate, DateTime toDate) {

        StringBuilder sqlBuilder = new StringBuilder();

        sqlBuilder.append("SELECT toucher.id as toucher_id, toucher.last_name as toucher, qc.id as qc_id, qc.last_name as qc, count(jtut.id) as volumn, sum(jtut.number_of_rework) as error, count(jtut.id) / sum(jtut.number_of_rework) as error_rate");
        sqlBuilder.append(" FROM job_team_user_task jtut");
        sqlBuilder.append(" inner join job_team_user jtu on jtut.job_team_user_id = jtu.id");
        sqlBuilder.append(" inner join jhi_user toucher on jtu.user_id = toucher.id");
        sqlBuilder.append(" inner join jhi_user qc on jtut.qc_id = qc.id");
        sqlBuilder.append(" where jtut.status = 'DONE' AND last_done_time between ? and ?");
        sqlBuilder.append(" group by toucher.id, toucher.last_name, qc.id, qc.last_name;");

        Query query = entityManager.createNativeQuery(sqlBuilder.toString());
        query.setParameter(1, fromDate.toString(LADateTimeUtil.DATETIME_FORMAT));
        query.setParameter(2, toDate.toString(LADateTimeUtil.DATETIME_FORMAT));
        List<Object[]> rows = query.getResultList();


        List<QualityDTO> report = new ArrayList<>();
        for (Object[] row : rows) {
            QualityDTO qualityDTO = new QualityDTO();
            qualityDTO.setRetoucherId(Long.parseLong(row[0].toString()));
            qualityDTO.setRetoucher(row[1].toString());
            qualityDTO.setQcId(Long.parseLong(row[2].toString()));
            qualityDTO.setQc(row[3].toString());
            qualityDTO.setVolumn(Long.parseLong(row[4].toString()));
            qualityDTO.setError(Long.parseLong(row[5].toString()));
            qualityDTO.setErrorRate(Double.parseDouble(row[6].toString()));
            report.add(qualityDTO);
        }
        QualitiReportResponseVM rs = new QualitiReportResponseVM();

        rs.setReport(report);
        return rs;

    }

    @Override
    public DeliveryQualityResponseVM getDeliveryQualityReportForUser(DateTime fromDate, DateTime toDate) {
        StringBuilder sqlBuilder = new StringBuilder();

        sqlBuilder.append("SELECT ju.id as userId, ju.last_name, p.id as projectId, p.name as project_name, j.id as jobId, j.name as job_name, count(jtut.id) as volumn, count(if(jtut.status='DONE',1,NULL)) as done, sum(jtut.number_of_rework) as error, count(jtut.id) / sum(jtut.number_of_rework) as error_rate, min(jtu.created_date) as received_date");
        sqlBuilder.append(" FROM job_team_user_task jtut");
        sqlBuilder.append(" inner join job_team_user jtu on jtut.job_team_user_id = jtu.id");
        sqlBuilder.append(" inner join jhi_user ju on jtu.user_id = ju.id");
        sqlBuilder.append(" inner join jhi_user_authority jua on jua.user_id = ju.id");
        sqlBuilder.append(" inner join job_team jt on jtu.job_team_id = jt.id");
        sqlBuilder.append(" inner join job j on jt.job_id = j.id");
        sqlBuilder.append(" inner join project p on p.id = j.project_id");
        sqlBuilder.append(" where jua.authority_name <> 'FREELANCER' AND jtut.created_date between ? and ? ");
        sqlBuilder.append(" group by ju.id, ju.last_name, p.id, p.name, j.id, j.name;");

        return getDeliveryReportFromQuery(sqlBuilder.toString(), fromDate, toDate);
    }

    @Override
    public DeliveryQualityResponseVM getDeliveryQualityReportForFreelancer(DateTime fromDate, DateTime toDate) {
        StringBuilder sqlBuilder = new StringBuilder();

        sqlBuilder.append("SELECT ju.id as userId, ju.last_name, p.id as projectId, p.name as project_name, j.id as jobId, j.name as job_name, count(jtut.id) as volumn, count(if(jtut.status='DONE',1,NULL)) as done, sum(jtut.number_of_rework) as error, count(jtut.id) / sum(jtut.number_of_rework) as error_rate, min(jtu.created_date) as received_date");
        sqlBuilder.append(" FROM job_team_user_task jtut");
        sqlBuilder.append(" inner join job_team_user jtu on jtut.job_team_user_id = jtu.id");
        sqlBuilder.append(" inner join jhi_user ju on jtu.user_id = ju.id");
        sqlBuilder.append(" inner join jhi_user_authority jua on jua.user_id = ju.id");
        sqlBuilder.append(" inner join job_team jt on jtu.job_team_id = jt.id");
        sqlBuilder.append(" inner join job j on jt.job_id = j.id");
        sqlBuilder.append(" inner join project p on p.id = j.project_id");
        sqlBuilder.append(" where jua.authority_name = 'FREELANCER' AND jtut.created_date between ? and ? ");
        sqlBuilder.append(" group by ju.id, ju.last_name, p.id, p.name, j.id, j.name;");

        return getDeliveryReportFromQuery(sqlBuilder.toString(), fromDate, toDate);

    }

    private DeliveryQualityResponseVM getDeliveryReportFromQuery(String sql,DateTime fromDate, DateTime toDate ) {
        Query query = entityManager.createNativeQuery(sql);

        query.setParameter(1, fromDate.toString(LADateTimeUtil.DATETIME_FORMAT));
        query.setParameter(2, toDate.toString(LADateTimeUtil.DATETIME_FORMAT));
        List<Object[]> rows = query.getResultList();


        List<DeliveryQualityReportDTO> report = new ArrayList<DeliveryQualityReportDTO>();
        for (Object[] row : rows) {
            DeliveryQualityReportDTO deliveryQualityReportDTO = new DeliveryQualityReportDTO();
            deliveryQualityReportDTO.setUserId(Long.parseLong(row[0].toString()));
            deliveryQualityReportDTO.setEmployee(row[1].toString());
            deliveryQualityReportDTO.setProjectId(Long.parseLong(row[2].toString()));
            deliveryQualityReportDTO.setProjectName(row[3].toString());
            deliveryQualityReportDTO.setJobId(Long.parseLong(row[4].toString()));
            deliveryQualityReportDTO.setJobName(row[5].toString());
            deliveryQualityReportDTO.setVolumn(Long.parseLong(row[6].toString()));
            deliveryQualityReportDTO.setDone(Long.parseLong(row[7].toString()));
            deliveryQualityReportDTO.setError(Long.parseLong(row[8].toString()));
            deliveryQualityReportDTO.setErrorRate(Double.parseDouble(row[9].toString()));
            deliveryQualityReportDTO.setReceivedDate(ZonedDateTime.parse(row[10].toString()));
            report.add(deliveryQualityReportDTO);
        }
        DeliveryQualityResponseVM rs = new DeliveryQualityResponseVM();
        rs.setReport(report);
        return rs;
    }

    @Override
    public CheckInResponseVM getCheckinReport(DateTime fromDate, DateTime toDate) {
        StringBuilder sqlBuilder = new StringBuilder();


        sqlBuilder.append("SELECT ju.last_name, DATE(jtutt.created_date) as date, min(jtutt.created_date) as checkin, max(jtutt.created_date) as checkout");
        sqlBuilder.append(" FROM job_team_user_task_tracking jtutt");
        sqlBuilder.append(" inner join jhi_user ju on jtutt.user_id = ju.id");
        sqlBuilder.append(" where jtutt.created_date between ? and ?");
        sqlBuilder.append(" group by ju.id, DATE(jtutt.created_date), jtutt.status");
        sqlBuilder.append(" having jtutt.status = 'TOCHECK';");

        Query query = entityManager.createNativeQuery(sqlBuilder.toString());
        query.setParameter(1, fromDate.toString(LADateTimeUtil.DATETIME_FORMAT));
        query.setParameter(2, toDate.toString(LADateTimeUtil.DATETIME_FORMAT));
        List<Object[]> rows = query.getResultList();


        List<CheckInReport> report = new ArrayList<CheckInReport>();
        for (Object[] row : rows) {
            CheckInReport checkInReport = new CheckInReport();
            checkInReport.setEmployee(row[0].toString());
            checkInReport.setDay(ZonedDateTime.parse(row[1].toString()));
            checkInReport.setCheckin(ZonedDateTime.parse(row[2].toString()));
            checkInReport.setCheckout(ZonedDateTime.parse(row[3].toString()));

            report.add(checkInReport);
        }
        CheckInResponseVM rs = new CheckInResponseVM();

        rs.setReport(report);
        return rs;
    }

}
