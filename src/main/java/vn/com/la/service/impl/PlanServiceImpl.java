package vn.com.la.service.impl;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.la.config.ApplicationProperties;
import vn.com.la.config.Constants;
import vn.com.la.domain.JobTeamUser;
import vn.com.la.domain.enumeration.FileStatusEnum;
import vn.com.la.repository.SequenceDataDao;
import vn.com.la.service.*;
import vn.com.la.service.dto.*;
import vn.com.la.service.dto.param.GetAllPlanParamDTO;
import vn.com.la.service.dto.param.GetJobPlanDetailParamDTO;
import vn.com.la.service.dto.param.UpdatePlanParamDTO;
import vn.com.la.service.util.LAStringUtil;
import vn.com.la.web.rest.vm.response.GetAllPlanResponseVM;
import vn.com.la.web.rest.vm.response.JobPlanDetailResponseVM;
import vn.com.la.web.rest.vm.response.UpdatePlanResponseVM;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.File;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class PlanServiceImpl implements PlanService {

    private final JobService jobService;
    private final JobTeamService jobTeamService;
    private final FileSystemHandlingService fileSystemHandlingService;
    private final JobTeamUserTaskService jobTeamUserTaskService;
    private final SequenceDataDao sequenceDataDao;
    private final JobTeamUserService jobTeamUserService;
    private final ProjectService projectService;
    private final ApplicationProperties applicationProperties;
    private final EntityManager em;


    public PlanServiceImpl(JobService jobService, JobTeamService jobTeamService, FileSystemHandlingService ftpService,
                           JobTeamUserTaskService jobTeamUserTaskService,
                           SequenceDataDao sequenceDataDao, JobTeamUserService jobTeamUserService,
                           ProjectService projectService, ApplicationProperties applicationProperties,
                           EntityManager em) {
        this.jobService = jobService;
        this.jobTeamService = jobTeamService;
        this.fileSystemHandlingService = ftpService;
        this.jobTeamUserTaskService = jobTeamUserTaskService;
        this.sequenceDataDao = sequenceDataDao;
        this.jobTeamUserService = jobTeamUserService;
        this.projectService = projectService;
        this.applicationProperties = applicationProperties;
        this.em = em;
    }

    @Override
    public JobPlanDetailResponseVM getPlanDetail(GetJobPlanDetailParamDTO params) {

        JobDTO job = jobService.findOne(params.getJobId());
        if (job != null) {
            if (job.getTotalFiles() == null) {
                job.setTotalFiles(Constants.ZERO.longValue());
            }

        }
        JobPlanDetailResponseVM rs = new JobPlanDetailResponseVM();
        rs.setJob(job);

        return rs;
    }

    @Override
    @Transactional(readOnly = false)
    public UpdatePlanResponseVM updatePlan(UpdatePlanParamDTO params) throws Exception {

        JobDTO job = jobService.findOne(params.getJobId());
        if (job != null) {

            job.setDeadline(params.getDeadline());
            job.setCustomerRequirements(params.getCustomerRequirements());
            if(BooleanUtils.isNotTrue(job.getStarted())) {
                job.setDeadline(params.getDeadline());
                job.setCustomerRequirements(params.getCustomerRequirements());
                job.setJobTasks(params.getTasks());
                job.setJobTeams(params.getTeams());
                job.setTotalFiles(params.getTotalFiles());
                if (job.getSequenceTask() == null) {
                    job.setSequenceTask(params.getSequenceTask());
                    job.setSequence(Constants.ONE);
                }

                job = jobService.save(job);

                // create backlog item folder inside todo, tocheck, done
                String toDoFolder = LAStringUtil.buildFolderPath(Constants.DASH + job.getProjectCode(), Constants.TO_DO, job.getName());
                fileSystemHandlingService.deleteDirectory(toDoFolder);
                fileSystemHandlingService.makeDirectory(toDoFolder);

                String toCheckFolder = LAStringUtil.buildFolderPath(Constants.DASH + job.getProjectCode(), Constants.TO_CHECK, job.getName());
                fileSystemHandlingService.deleteDirectory(toCheckFolder);
                fileSystemHandlingService.makeDirectory(toCheckFolder);

                String doneFolder = LAStringUtil.buildFolderPath(Constants.DASH + job.getProjectCode(), Constants.DONE, job.getName());
                fileSystemHandlingService.deleteDirectory(doneFolder);
                fileSystemHandlingService.makeDirectory(doneFolder);

                String deliveryFolder = LAStringUtil.buildFolderPath(Constants.DASH + job.getProjectCode(), Constants.DELIVERY, job.getName());
                fileSystemHandlingService.deleteDirectory(deliveryFolder);
                fileSystemHandlingService.makeDirectory(deliveryFolder);
                // move files from backlogs to to-do

                long totalFilesInBacklogItem = job.getTotalFiles();
                // list file from backlog item
                String backLogItemPath = LAStringUtil.buildFolderPath(Constants.DASH + job.getProjectCode(), Constants.BACK_LOGS, job.getName());
                List<File> files = fileSystemHandlingService.listFileRecursiveFromPath(backLogItemPath);
                int iFile = 0;

                Set<JobTeamDTO> jobTeamDTOs = job.getJobTeams();
                for (JobTeamDTO jobTeamDTO : jobTeamDTOs) {
                    long totalFilesForJobTeam = 0;
                    if (jobTeamDTO.getJobTeamUsers() != null) {


                        // create user folder (as login name) into todo, tocheck, done of backlog item
                        for (JobTeamUserDTO jobTeamUserDTO : jobTeamDTO.getJobTeamUsers()) {

                            if(jobTeamUserDTO.getJobTeamId() == null) {
                                jobTeamUserDTO.setJobTeamId(jobTeamDTO.getId());
                            }

                            if(jobTeamUserDTO.getId() == null) {
                                jobTeamUserDTO = jobTeamUserService.save(jobTeamUserDTO);
                            }

                            // To-do folder
                            String toDoFolderOfUser = LAStringUtil.buildFolderPath(Constants.DASH + job.getProjectCode(), Constants.TO_DO, job.getName(), jobTeamUserDTO.getUserLogin());
                            fileSystemHandlingService.deleteDirectory(toDoFolderOfUser);
                            fileSystemHandlingService.makeDirectory(toDoFolderOfUser);

                            // to-check folder
                            String toCheckFolderOfUser = LAStringUtil.buildFolderPath(Constants.DASH + job.getProjectCode(), Constants.TO_CHECK, job.getName(), jobTeamUserDTO.getUserLogin());
                            fileSystemHandlingService.deleteDirectory(toCheckFolderOfUser);
                            fileSystemHandlingService.makeDirectory(toCheckFolderOfUser);

                            // done folder
                            String doneFolderOfUser = LAStringUtil.buildFolderPath(Constants.DASH + job.getProjectCode(), Constants.DONE, job.getName(), jobTeamUserDTO.getUserLogin());
                            fileSystemHandlingService.deleteDirectory(doneFolderOfUser);
                            fileSystemHandlingService.makeDirectory(doneFolderOfUser);

                            // move files from backlogs into todo folder
                            if (iFile >= files.size()) {
                                jobTeamDTO.setTotalFiles(new Long(Constants.ZERO));
                            } else {

                                if (jobTeamDTO.getTotalFiles() != null) {
                                    long iActualTotalFiles = Constants.ZERO;
                                    Set<JobTeamUserTaskDTO> jobTeamUserTaskDTOs = new HashSet<>();
                                    for (iActualTotalFiles = Constants.ONE; iActualTotalFiles <= jobTeamUserDTO.getTotalFiles(); iActualTotalFiles++) {

                                        File file = files.get(iFile++);
                                        if (file.isDirectory()) {
                                            continue;
                                        }

                                        if(iActualTotalFiles > jobTeamUserDTO.getTotalFiles()) {
                                            break;
                                        }

                                        String remoteFileName = file.getName();

                                        JobTeamUserTaskDTO jobTeamUserTaskDTO = new JobTeamUserTaskDTO();
                                        jobTeamUserTaskDTO.setFilePath(toDoFolderOfUser);

                                        jobTeamUserTaskDTO.setJobTeamUserId(jobTeamUserDTO.getId()); // assignee

                                        String filePath = file.getParent();
                                        String originalRelativeFilePath = LAStringUtil.removeRootPath(filePath, this.applicationProperties.getRootFolder());

                                        jobTeamUserTaskDTO.setOriginalFilePath(originalRelativeFilePath);
                                        jobTeamUserTaskDTO.setOriginalFileName(remoteFileName);

                                        jobTeamUserTaskDTO.setStatus(FileStatusEnum.TODO);

                                        // setup file name
                                        String newFileName = job.getProjectCode() + Constants.UNDERSCORE + job.getName() + Constants.UNDERSCORE + sequenceDataDao.nextJobTeamUserTaskId() + Constants.UNDERSCORE + remoteFileName;
                                        jobTeamUserTaskDTO.setFileName(newFileName);

                                        jobTeamUserTaskDTO.setJobTeamUserId(jobTeamUserDTO.getId());
                                        jobTeamUserTaskDTOs.add(jobTeamUserTaskDTO);
                                        fileSystemHandlingService.copy(file.getPath().substring(this.applicationProperties.getRootFolder().length()), toDoFolderOfUser, newFileName);

                                        totalFilesForJobTeam++;
                                        if (iFile >= files.size()) {

                                            break;
                                        }
                                    }
                                    jobTeamUserDTO.setTotalFiles(iActualTotalFiles - Constants.ONE);
                                    jobTeamUserDTO.setJobTeamUserTasks(jobTeamUserTaskDTOs);

                                    System.out.println("Test");
                                }

                            }
                        }
                    }
                    jobTeamDTO.setTotalFiles(totalFilesForJobTeam);
                }

            }else {

            }

            // re-update job
            job = jobService.save(job);

        }

        UpdatePlanResponseVM rs = new UpdatePlanResponseVM();
        rs.setJob(job);

        return rs;
    }

    @Override
    public GetAllPlanResponseVM getAllPlans(GetAllPlanParamDTO params) {

        GetAllPlanResponseVM rs = new GetAllPlanResponseVM();

        if(PlanViewEnumDTO.PROJECT == params.getView()) {
            rs.setProjects(buildPlansByProject());
        }else if(PlanViewEnumDTO.USER == params.getView()) {
            rs.setTeams(buildPlansByTeams());
        }

        return rs;
    }

    private List<ProjectDTO> buildPlansByProject() {
        List<ProjectDTO> projectDTOs = projectService.findAll();

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT ");
        sqlBuilder.append("     sum(case when jtut.status IN ('TODO','REWORK') then 1 else 0 end) as TODO,");
        sqlBuilder.append("     sum(case when jtut.status = 'TOCHECK' then 1 else 0 end) as TOCHECK,");
        sqlBuilder.append("     sum(case when jtut.status = 'DONE' then 1 else 0 end) as DONE,");
        sqlBuilder.append("     sum(case when jtut.last_delivery_time IS NOT NULL then 1 else 0 end) as DELIVERY");
        sqlBuilder.append(" FROM job_team_user_task jtut");
        sqlBuilder.append(" inner join job_team_user jtu on jtut.job_team_user_id = jtu.id");
        sqlBuilder.append(" inner join jhi_user ju on jtu.user_id = ju.id");
        sqlBuilder.append(" WHERE jtu.id = ?");
        Query query = em.createNativeQuery(sqlBuilder.toString());

        for(ProjectDTO projectDTO: projectDTOs) {

            Long totalFilesForProject = 0L;
            Long totalToDoFilesForProject = 0L;
            Long totalToCheckFilesForProject = 0L;
            Long totalDoneFilesForProject = 0L;
            Long totalDeliveryFilesForProject = 0L;

            for(JobDTO jobDTO: projectDTO.getJobs()) {

                Long totalToDoFilesForJob = 0L;
                Long totalToCheckFilesForJob = 0L;
                Long totalDoneFilesForJob = 0L;
                Long totalDeliveryFilesForJob = 0L;

                for(JobTeamDTO jobTeamDTO: jobDTO.getJobTeams()) {

                    Long totalToDoFilesForTeam = 0L;
                    Long totalToCheckFilesForTeam = 0L;
                    Long totalDoneFilesForTeam = 0L;
                    Long totalDeliveryFilesForTeam = 0L;

                    for(JobTeamUserDTO jobTeamUserDTO: jobTeamDTO.getJobTeamUsers()) {

                        query.setParameter(1, jobTeamUserDTO.getId());
                        List<Object[]> rows = query.getResultList();

                        for(Object[] row: rows) {
                            jobTeamUserDTO.setTotalToDoFiles(Long.parseLong(row[0].toString()));
                            jobTeamUserDTO.setTotalToCheckFiles(Long.parseLong(row[1].toString()));
                            jobTeamUserDTO.setTotalDoneFiles(Long.parseLong(row[2].toString()));
                            jobTeamUserDTO.setTotalDeliveryFiles(Long.parseLong(row[3].toString()));

                            totalToDoFilesForTeam += Optional.ofNullable(jobTeamUserDTO.getTotalToDoFiles()).orElse(0L);
                            totalToCheckFilesForTeam += Optional.ofNullable(jobTeamUserDTO.getTotalToCheckFiles()).orElse(0L);
                            totalDoneFilesForTeam += Optional.ofNullable(jobTeamUserDTO.getTotalDoneFiles()).orElse(0L);
                            totalDeliveryFilesForTeam += Optional.ofNullable(jobTeamUserDTO.getTotalDeliveryFiles()).orElse(0L);
                        }
                    }

                    totalToDoFilesForJob += totalToDoFilesForTeam;
                    totalToCheckFilesForJob += totalToCheckFilesForTeam;
                    totalDoneFilesForJob += totalDoneFilesForTeam;
                    totalDeliveryFilesForJob += totalDeliveryFilesForTeam;

                    jobTeamDTO.setTotalToDoFiles(totalToDoFilesForTeam);
                    jobTeamDTO.setTotalToCheckFiles(totalToCheckFilesForTeam);
                    jobTeamDTO.setTotalDoneFiles(totalDoneFilesForTeam);
                    jobTeamDTO.setTotalDeliveryFiles(totalDeliveryFilesForTeam);
                }

                totalFilesForProject += jobDTO.getTotalFiles();
                totalToDoFilesForProject += totalToDoFilesForJob;
                totalToCheckFilesForProject += totalToCheckFilesForJob;
                totalDoneFilesForProject += totalDoneFilesForJob;
                totalDeliveryFilesForProject += totalDeliveryFilesForJob;

                jobDTO.setTotalToDoFiles(totalToDoFilesForJob);
                jobDTO.setTotalToCheckFiles(totalToCheckFilesForJob);
                jobDTO.setTotalDoneFiles(totalDoneFilesForJob);
                jobDTO.setTotalDeliveryFiles(totalDeliveryFilesForJob);
            }

            projectDTO.setTotalFiles(totalFilesForProject);
            projectDTO.setTotalToDoFiles(totalToDoFilesForProject);
            projectDTO.setTotalToCheckFiles(totalToCheckFilesForProject);
            projectDTO.setTotalDoneFiles(totalDoneFilesForProject);
            projectDTO.setTotalDeliveryFiles(totalDeliveryFilesForProject);
        }
        return projectDTOs;
    }

    private Map<Long, PlanTeamDTO> buildPlansByTeams() {

        Map<Long, PlanTeamDTO> teams = new HashMap<>();

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT jt.team_id as team_id, t.name as team_name,  jtu.user_id as user_id, jhi_user.last_name, p.id as project_id, p.name as project_name, j.id as job_id, j.name as job_name, ");
        sqlBuilder.append(" jtu.total_files as TotalFiles,");
        sqlBuilder.append("     sum(case when jtut.status IN ('TODO','REWORK') then 1 else 0 end) as TODO,");
        sqlBuilder.append("     sum(case when jtut.status = 'TOCHECK' then 1 else 0 end) as TOCHECK,");
        sqlBuilder.append("     sum(case when jtut.status = 'DONE' then 1 else 0 end) as DONE,");
        sqlBuilder.append("     sum(case when jtut.last_delivery_time IS NOT NULL then 1 else 0 end) as DELIVERY");
        sqlBuilder.append(" from job_team jt");
        sqlBuilder.append(" inner join team t on jt.team_id = t.id");
        sqlBuilder.append(" inner join job j on j.id = jt.job_id");
        sqlBuilder.append(" inner join project p on p.id = j.project_id");
        sqlBuilder.append(" inner join job_team_user jtu on jtu.job_team_id = jt.id");
        sqlBuilder.append(" inner join jhi_user jhi_user on jhi_user.id = jtu.user_id");
        sqlBuilder.append(" inner join job_team_user_task jtut on jtut.job_team_user_id = jtu.id");
        sqlBuilder.append(" group by jt.team_id, t.name, jtu.user_id, jhi_user.last_name, p.id, p.name, j.id, j.name, jtu.total_files");
        Query query = em.createNativeQuery(sqlBuilder.toString());

        List<Object[]> rows = query.getResultList();
        for(Object[] row: rows) {
            PlanTeamDTO team = null;
            Long teamId = Long.parseLong(row[0].toString());
            if(teams.containsKey(teamId)) {
                team = teams.get(teamId);
            }else {
                team = new PlanTeamDTO();
                team.setTeamId(teamId);
                if(row[1] != null) {
                    team.setTeamName(row[1].toString());
                }
                teams.put(teamId, team);
            }
            team.update(row);
        }

        return teams;
    }

}

