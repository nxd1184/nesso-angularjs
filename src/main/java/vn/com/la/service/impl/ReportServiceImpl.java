package vn.com.la.service.impl;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import vn.com.la.config.Constants;
import vn.com.la.service.JobService;
import vn.com.la.service.JobTeamUserTaskService;
import vn.com.la.service.ReportService;
import vn.com.la.service.TimesheetService;
import vn.com.la.service.dto.*;
import vn.com.la.service.dto.param.DashboardReportParam;
import vn.com.la.service.util.LADateTimeUtil;
import vn.com.la.web.rest.vm.response.*;


import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ReportServiceImpl implements ReportService {

    private final JobService jobService;
    private final JobTeamUserTaskService jobTeamUserTaskService;
    private final TimesheetService timesheetService;

    private final EntityManager entityManager;

    public ReportServiceImpl(JobService jobService, JobTeamUserTaskService jobTeamUserTaskService,
                             EntityManager entityManager,
                             TimesheetService timesheetService) {
        this.jobService = jobService;
        this.jobTeamUserTaskService = jobTeamUserTaskService;
        this.entityManager = entityManager;
        this.timesheetService = timesheetService;
    }

    @Override
    public DashboardResponseVM getDashboardData(DashboardReportParam param) {
        DashboardResponseVM rs = new DashboardResponseVM();

        rs.setTotalReceive(jobService.getTotalReceiverByDateTime(param.getFromDate(), param.getToDate()));
        rs.setTotalToDo(jobTeamUserTaskService.countByStatusAndDateRange(Constants.TO_DO_STATUS_LIST, LADateTimeUtil.zonedDateTimeToInstant(param.getFromDate()), LADateTimeUtil.zonedDateTimeToInstant(param.getToDate())));
        rs.setTotalToCheck(jobTeamUserTaskService.countByStatusAndDateRange(Constants.TO_CHECK_STATUS_LIST, LADateTimeUtil.zonedDateTimeToInstant(param.getFromDate()), LADateTimeUtil.zonedDateTimeToInstant(param.getToDate())));
        rs.setTotalDone(jobTeamUserTaskService.countByStatusAndDateRange(Constants.DONE_STATUS_LIST, LADateTimeUtil.zonedDateTimeToInstant(param.getFromDate()), LADateTimeUtil.zonedDateTimeToInstant(param.getToDate())));
        rs.setTotalRework(jobTeamUserTaskService.sumNumberOfReworkByStatusInAndLastReworkTimeIsBetween(Constants.REWORK_STATUS_LIST, param.getFromDate(), param.getToDate()));
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

        sqlBuilder.append("SELECT sum(task.task_credit) as total FROM job_team_user_task jtut ");
        sqlBuilder.append(" inner join job_team_user jtu on jtut.job_team_user_id = jtu.id");
        sqlBuilder.append(" inner join job_team jt on jtu.job_team_id = jt.id");
        sqlBuilder.append(" inner join job j on jt.job_id = j.id");
        sqlBuilder.append(" left join job_task job_task on job_task.job_id = j.id");
        sqlBuilder.append(" left join task task on job_task.task_id = task.id");
        sqlBuilder.append(" where jtut.status = 'DONE' AND MONTH(jtut.last_done_time) = ? AND YEAR(jtut.last_done_time) = ?");

        Query query = entityManager.createNativeQuery(sqlBuilder.toString());

        // for this month
        try {
            query.setParameter(1, now.getMonthOfYear());
            query.setParameter(2, now.getYear());
            Optional opt = Optional.ofNullable(query.getSingleResult());
            Object singleResult = opt.orElse(0L);
            if (singleResult != null) {
                rs.setCountTotalDoneForThisMonth(Long.parseLong(singleResult.toString()));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // for last month
        query = entityManager.createNativeQuery(sqlBuilder.toString());
        try {
            query.setParameter(1, now.minusMonths(1).getMonthOfYear());
            query.setParameter(2, now.minusMonths(1).getYear());
            Optional value = Optional.ofNullable(query.getSingleResult());
            Object singleResult = value.orElse(0L);
            if (singleResult != null) {
                rs.setCountTotalDoneForLastMonth(Long.parseLong(singleResult.toString()));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // for best month
        sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT MONTH(jtut.last_done_time) as MONTH, YEAR(jtut.last_done_time) as YEAR, sum(task.task_credit) as total_credit FROM job_team_user_task jtut ");
        sqlBuilder.append(" inner join job_team_user jtu on jtut.job_team_user_id = jtu.id");
        sqlBuilder.append(" inner join job_team jt on jtu.job_team_id = jt.id");
        sqlBuilder.append(" inner join job j on jt.job_id = j.id");
        sqlBuilder.append(" inner join job_task job_task on job_task.job_id = j.id");
        sqlBuilder.append(" inner join task task on job_task.task_id = task.id");
        sqlBuilder.append(" where jtut.status = 'DONE'");
        sqlBuilder.append(" GROUP BY MONTH(jtut.last_done_time), YEAR(jtut.last_done_time)");
        sqlBuilder.append(" ORDER BY total_credit desc");
        try {
            query = entityManager.createNativeQuery(sqlBuilder.toString());
            List<Object[]> rows = query.getResultList();
            if (rows != null && rows.size() > 0) {
                if(rows.get(0)[2] != null) {
                    rs.setCountTotalDoneForBestMonth(Long.parseLong(rows.get(0)[2].toString()));
                }
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
                userProductivityDTOS.add(userProductivityDTO);

            }
            rs.setUserProductivityList(userProductivityDTOS);
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        return rs;
    }

    @Override
    public ProductionBonusReportResponseVM getProductBonusReport(DateTime fromDate, DateTime toDate) {

        List<ProductionBonusDTO> report = getListProductionBonusReport(fromDate, toDate);
        //TODO: Fake data:
        /*for (int i = 0; i < 5; i++) {
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
        }*/

        ProductionBonusReportResponseVM rs = new ProductionBonusReportResponseVM();

        rs.setReport(report);
        return rs;
    }

    @Override
    public QualitiReportResponseVM getQualityReport(DateTime fromDate, DateTime toDate) {

        StringBuilder sqlBuilder = new StringBuilder();

        sqlBuilder.append("SELECT toucher.id as toucher_id, toucher.last_name as toucher, qc.id as qc_id, qc.last_name as qc, count(jtut.id) as volumn, sum(jtut.number_of_rework) as error, (count(jtut.id) / sum(jtut.number_of_rework))*100 as error_rate");
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
            if (row[4] != null) {
                qualityDTO.setVolumn(Long.parseLong(row[4].toString()));
            } else {
                qualityDTO.setVolumn(0L);
            }

            if (row[5] != null) {
                qualityDTO.setError(Long.parseLong(row[5].toString()));
            } else {
                qualityDTO.setError(0L);
            }

            if (row[6] != null) {
                qualityDTO.setErrorRate(Double.parseDouble(row[6].toString()));
            } else {
                qualityDTO.setErrorRate(0d);
            }


            report.add(qualityDTO);
        }

        /*for (int i = 0; i < 10; i++ ) {
            QualityDTO qualityDTO = new QualityDTO();
            qualityDTO.setRetoucherId(RandomUtils.nextLong(0, 9));
            qualityDTO.setRetoucher("Retoucher " + qualityDTO.getRetoucherId());
            qualityDTO.setQcId(RandomUtils.nextLong(0, 9));
            qualityDTO.setQc("QC " + qualityDTO.getQcId());
            qualityDTO.setVolumn(RandomUtils.nextLong(100, 200));
            qualityDTO.setError(RandomUtils.nextLong(0, 30));
            qualityDTO.setErrorRate((double) (qualityDTO.getError()*1.0/qualityDTO.getVolumn()));
            report.add(qualityDTO);
        }*/
        QualitiReportResponseVM rs = new QualitiReportResponseVM();

        rs.setReport(report);
        return rs;

    }

    @Override
    public DeliveryQualityResponseVM getDeliveryQualityReportForUser(DateTime fromDate, DateTime toDate) {
        StringBuilder sqlBuilder = new StringBuilder();

        sqlBuilder.append("SELECT ju.id as userId, ju.last_name, p.id as projectId, p.name as project_name, j.id as jobId, j.name as job_name, count(jtut.id) as volumn, count(if(jtut.status='DONE',1,NULL)) as done, sum(jtut.number_of_rework) as error, (count(jtut.id) / sum(jtut.number_of_rework))*100 as error_rate, min(jtu.created_date) as received_date");
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

        sqlBuilder.append("SELECT ju.id as userId, ju.last_name, p.id as projectId, p.name as project_name, j.id as jobId, j.name as job_name, count(jtut.id) as volumn, count(if(jtut.status='DONE',1,NULL)) as done, sum(jtut.number_of_rework) as error, (count(jtut.id) / sum(jtut.number_of_rework))*100 as error_rate, min(jtu.created_date) as received_date");
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

    private DeliveryQualityResponseVM getDeliveryReportFromQuery(String sql, DateTime fromDate, DateTime toDate) {
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
            if (row[8] != null) {
                deliveryQualityReportDTO.setError(Long.parseLong(row[8].toString()));
            } else {
                deliveryQualityReportDTO.setError(0L);
            }
            if (row[9] != null) {
                deliveryQualityReportDTO.setErrorRate(Double.parseDouble(row[9].toString()));
            } else {
                deliveryQualityReportDTO.setErrorRate(0d);
            }
            deliveryQualityReportDTO.setReceivedDate(convertByteArrayToInstant((byte[]) row[10]));
            report.add(deliveryQualityReportDTO);
        }

        /*for (int i = 0 ; i < 10; i++) {
            DeliveryQualityReportDTO deliveryQualityReportDTO = new DeliveryQualityReportDTO();
            deliveryQualityReportDTO.setUserId(new Long(i));
            deliveryQualityReportDTO.setEmployee("User" + i);
            deliveryQualityReportDTO.setProjectId(new Long(i));
            deliveryQualityReportDTO.setProjectName("Project " + i);
            deliveryQualityReportDTO.setJobId(new Long(i));
            deliveryQualityReportDTO.setJobName("Job " + 1);
            deliveryQualityReportDTO.setVolumn(RandomUtils.nextLong(0, 200));
            deliveryQualityReportDTO.setDone(RandomUtils.nextLong(0, 100));
            deliveryQualityReportDTO.setError(RandomUtils.nextLong(0, 50));
            deliveryQualityReportDTO.setErrorRate(new Double(deliveryQualityReportDTO.getError()/deliveryQualityReportDTO.getVolumn()));
            deliveryQualityReportDTO.setReceivedDate(Instant.now());
            deliveryQualityReportDTO.setReturnDate(Instant.now());
            report.add(deliveryQualityReportDTO);
        }*/

        DeliveryQualityResponseVM rs = new DeliveryQualityResponseVM();
        rs.setReport(report);
        return rs;
    }

    @Override
    public CheckInResponseVM getCheckinReport(DateTime fromDate, DateTime toDate) {

        List<TimesheetDTO> timesheetDTOs = timesheetService.getTimesheetReport(fromDate.withTimeAtStartOfDay().toDate(), toDate.withTimeAtStartOfDay().toDate());

        List<CheckInReport> report = new ArrayList<CheckInReport>();
        for (TimesheetDTO shift : timesheetDTOs) {
            CheckInReport checkInReport = new CheckInReport();
            checkInReport.setEmployee(shift.getName());
            checkInReport.setDay(shift.getDate().toInstant());
            checkInReport.setCheckin(shift.getCheckInTime().toInstant());
            if(shift.getCheckOutTime() != null) {
                checkInReport.setCheckout(shift.getCheckOutTime().toInstant());
            }
            checkInReport.setUserId(shift.getUserId());
            report.add(checkInReport);
        }

        CheckInResponseVM rs = new CheckInResponseVM();

        rs.setReport(report);
        return rs;
    }

//    @Override
//    public CheckInResponseVM getCheckinReport(DateTime fromDate, DateTime toDate) {
//        StringBuilder sqlBuilder = new StringBuilder();
//
//
//        sqlBuilder.append("SELECT ju.last_name, DATE(jtutt.created_date) as date, min(jtutt.created_date) as checkin, max(jtutt.created_date) as checkout, ju.id as userId");
//        sqlBuilder.append(" FROM job_team_user_task_tracking jtutt");
//        sqlBuilder.append(" inner join jhi_user ju on jtutt.user_id = ju.id");
//        sqlBuilder.append(" where jtutt.created_date between ? and ?");
//        sqlBuilder.append(" group by ju.id, DATE(jtutt.created_date), jtutt.status");
//        sqlBuilder.append(" having jtutt.status = 'TOCHECK';");
//
//        Query query = entityManager.createNativeQuery(sqlBuilder.toString());
//        query.setParameter(1, fromDate.toString(LADateTimeUtil.DATETIME_FORMAT));
//        query.setParameter(2, toDate.toString(LADateTimeUtil.DATETIME_FORMAT));
//        List<Object[]> rows = query.getResultList();
//
//
//        List<CheckInReport> report = new ArrayList<CheckInReport>();
//        for (Object[] row : rows) {
//            CheckInReport checkInReport = new CheckInReport();
//            checkInReport.setEmployee(row[0].toString());
//            checkInReport.setDay(convertStringDateToInstant(row[1].toString()));
//            checkInReport.setCheckin(convertByteArrayToInstant((byte[]) row[2]));
//            checkInReport.setCheckout(convertByteArrayToInstant((byte[]) row[3]));
//            checkInReport.setUserId(Long.parseLong(row[4].toString()));
//            report.add(checkInReport);
//        }
//        //TODO: Fake data
//        /*for (int i = 0; i < 10; i++) {
//            CheckInReport checkInReport = new CheckInReport();
//            checkInReport.setUserId(RandomUtils.nextLong(0, 9));
//            checkInReport.setEmployee("User " + checkInReport.getUserId());
//            checkInReport.setDay(Instant.now().plusSeconds(RandomUtils.nextInt(0, 4)*37440));
//            checkInReport.setCheckin(Instant.now());
//            checkInReport.setCheckout(Instant.now().plusSeconds(RandomUtils.nextInt(3, 6)*3600).plusSeconds(RandomUtils.nextInt(0, 50)*60));
//            report.add(checkInReport);
//        }*/
//
//        CheckInResponseVM rs = new CheckInResponseVM();
//
//        rs.setReport(report);
//        return rs;
//    }

    public ProjectMemberReportResponseVM getProjectMemberReport(DateTime fromDate, DateTime toDate) {
        List<ProductionBonusDTO> productionBonusDTOS = getListProductionBonusReport(fromDate, toDate);
        ProjectMemberDTO report = ProjectMemberDTO.processDataset(productionBonusDTOS);

        ProjectMemberReportResponseVM rs = new ProjectMemberReportResponseVM();

        rs.setReport(report);
        return rs;
    }

    private List<ProductionBonusDTO> getListProductionBonusReport(DateTime fromDate, DateTime toDate) {
        StringBuilder sqlBuilder = new StringBuilder();

        sqlBuilder.append("SELECT ju.id, ju.last_name, p.id as project_id, p.name as project_name, j.id as job_id, j.name as job_name,");
        sqlBuilder.append(" count(distinct jtut.id) as volumn,");
        sqlBuilder.append(" (");
        sqlBuilder.append("    SELECT SUM(task.task_credit) as credit");
        sqlBuilder.append("    from job");
        sqlBuilder.append("    inner join job_task job_task on job_task.job_id = job.id");
        sqlBuilder.append("    inner join task task on job_task.task_id = task.id");
        sqlBuilder.append("    where job.id = j.id");
        sqlBuilder.append(" ) as credit,");
        sqlBuilder.append(" sum(task.task_credit) as total_credit");
        sqlBuilder.append(" FROM job_team_user_task jtut");
        sqlBuilder.append(" inner join job_team_user jtu on jtut.job_team_user_id = jtu.id");
        sqlBuilder.append(" inner join jhi_user ju on jtu.user_id = ju.id");
        sqlBuilder.append(" inner join job_team jt on jtu.job_team_id = jt.id");
        sqlBuilder.append(" inner join job j on jt.job_id = j.id");
        sqlBuilder.append(" left join job_task job_task on job_task.job_id = j.id");
        sqlBuilder.append(" inner join project p on p.id = j.project_id");
        sqlBuilder.append(" left join task task on job_task.task_id = task.id");
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

            productionBonusDTO.setCredit(Long.parseLong(Optional.ofNullable(row[7]).map(
                Object::toString
            ).orElse("0")));
            productionBonusDTO.setTotalCredit(Long.parseLong(Optional.ofNullable(row[8]).map(Object::toString).orElse("0")));

            report.add(productionBonusDTO);
        }
        ;
        return report;
    }

    private Instant convertByteArrayToInstant(byte[] bytes) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime ld = LocalDateTime.parse(new String(bytes, StandardCharsets.US_ASCII), fmt);
        Instant instant = ld.toInstant(ZoneOffset.UTC);
        return instant;
    }

    private Instant convertStringDateToInstant(String str) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate ld = LocalDate.parse(str, fmt);
        Instant instant = ld.atStartOfDay(ZoneOffset.UTC).toInstant();
        return instant;
    }
    public ReportXlsResponseVM exportReportToXls(ReportDTO reportDTO) {
        ReportXlsResponseVM responseVM = new ReportXlsResponseVM();
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Nesso Report");
        int rowNum = 0;

        for (HashMap<String, String> rowItem : reportDTO.getRows()) {
            Row row = sheet.createRow(rowNum++);
            int colNum = 0;
            boolean isBorder = false;
            if (rowItem.containsKey("index") && rowItem.get("index") != null) {
                isBorder = true;
            }
            for (String column: reportDTO.getColumns()) {
                Cell cell = row.createCell(colNum++);
                String value = (rowItem.containsKey(column))? rowItem.get(column) : "";
                cell.setCellValue(value);
                if (isBorder) {
                    XSSFCellStyle cellStyle = workbook.createCellStyle();
                    XSSFColor color = new XSSFColor(new java.awt.Color(0, 0, 0));
//                cellStyle.setBorderColor(XSSFCellBorder.BorderSide.BOTTOM, color);
                    cellStyle.setTopBorderColor(color);
                    cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
                    cell.setCellStyle(cellStyle);
                }
            }
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            workbook.write(bos);
            byte[] bytes = bos.toByteArray();
            responseVM.setBytes(bytes);
            responseVM.setFileName(reportDTO.getName());
        } catch (IOException e) {
            e.printStackTrace();
            responseVM.setSuccess(false);
        } finally {
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return responseVM;
    }

}
