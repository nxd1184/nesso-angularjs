package vn.com.la.service.impl;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.la.config.ApplicationProperties;
import vn.com.la.config.Constants;
import vn.com.la.domain.*;
import vn.com.la.domain.enumeration.FileStatusEnum;
import vn.com.la.repository.SequenceDataDao;
import vn.com.la.service.*;
import vn.com.la.service.dto.*;
import vn.com.la.service.dto.param.*;
import vn.com.la.service.util.LAStringUtil;
import vn.com.la.web.rest.errors.CustomParameterizedException;
import vn.com.la.web.rest.vm.response.*;

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
    private final UserService userService;
    private final TeamService teamService;
    private final CacheManager cacheManager;


    public PlanServiceImpl(JobService jobService, JobTeamService jobTeamService, FileSystemHandlingService ftpService,
                           JobTeamUserTaskService jobTeamUserTaskService,
                           SequenceDataDao sequenceDataDao, JobTeamUserService jobTeamUserService,
                           ProjectService projectService, ApplicationProperties applicationProperties,
                           EntityManager em, UserService userService, TeamService teamService,
                           CacheManager cacheManager) {
        this.jobService = jobService;
        this.jobTeamService = jobTeamService;
        this.fileSystemHandlingService = ftpService;
        this.jobTeamUserTaskService = jobTeamUserTaskService;
        this.sequenceDataDao = sequenceDataDao;
        this.jobTeamUserService = jobTeamUserService;
        this.projectService = projectService;
        this.applicationProperties = applicationProperties;
        this.em = em;
        this.userService = userService;
        this.teamService = teamService;
        this.cacheManager = cacheManager;
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

        JobDTO storedJob = jobService.findOne(params.getJobId());
        if (storedJob != null) {

            storedJob.setDeadline(params.getDeadline());
            storedJob.setCustomerRequirements(params.getCustomerRequirements());
            if(BooleanUtils.isNotTrue(storedJob.getStarted())) {
                storedJob.setDeadline(params.getDeadline());
                storedJob.setCustomerRequirements(params.getCustomerRequirements());
                storedJob.setJobTasks(params.getTasks());
                storedJob.setJobTeams(params.getTeams());
                storedJob.setTotalFiles(params.getTotalFiles());
                if (storedJob.getSequenceTask() == null) {
                    storedJob.setSequenceTask(params.getSequenceTask());
                    storedJob.setSequence(Constants.ONE);
                }

                storedJob = jobService.save(storedJob);

                // create backlog item folder inside todo, tocheck, done
                String toDoFolder = LAStringUtil.buildFolderPath(Constants.DASH + storedJob.getProjectCode(), Constants.TO_DO, storedJob.getName());
                fileSystemHandlingService.deleteDirectory(toDoFolder);
                fileSystemHandlingService.makeDirectory(toDoFolder);

                String toCheckFolder = LAStringUtil.buildFolderPath(Constants.DASH + storedJob.getProjectCode(), Constants.TO_CHECK, storedJob.getName());
                fileSystemHandlingService.deleteDirectory(toCheckFolder);
                fileSystemHandlingService.makeDirectory(toCheckFolder);

                String doneFolder = LAStringUtil.buildFolderPath(Constants.DASH + storedJob.getProjectCode(), Constants.DONE, storedJob.getName());
                fileSystemHandlingService.deleteDirectory(doneFolder);
                fileSystemHandlingService.makeDirectory(doneFolder);

                String deliveryFolder = LAStringUtil.buildFolderPath(Constants.DASH + storedJob.getProjectCode(), Constants.DELIVERY, storedJob.getName());
                fileSystemHandlingService.deleteDirectory(deliveryFolder);
                fileSystemHandlingService.makeDirectory(deliveryFolder);
                // move files from backlogs to to-do

                long totalFilesInBacklogItem = storedJob.getTotalFiles();
                // list file from backlog item
                String backLogItemPath = LAStringUtil.buildFolderPath(Constants.DASH + storedJob.getProjectCode(), Constants.BACK_LOGS, storedJob.getName());
                List<File> files = fileSystemHandlingService.listFileRecursiveFromPath(backLogItemPath);
                int iFile = 0;

                Set<JobTeamDTO> jobTeamDTOs = storedJob.getJobTeams();
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
                            String toDoFolderOfUser = LAStringUtil.buildFolderPath(Constants.DASH + storedJob.getProjectCode(), Constants.TO_DO, storedJob.getName(), jobTeamUserDTO.getUserLogin());
                            fileSystemHandlingService.deleteDirectory(toDoFolderOfUser);
                            fileSystemHandlingService.makeDirectory(toDoFolderOfUser);

                            // to-check folder
                            String toCheckFolderOfUser = LAStringUtil.buildFolderPath(Constants.DASH + storedJob.getProjectCode(), Constants.TO_CHECK, storedJob.getName(), jobTeamUserDTO.getUserLogin());
                            fileSystemHandlingService.deleteDirectory(toCheckFolderOfUser);
                            fileSystemHandlingService.makeDirectory(toCheckFolderOfUser);

                            // done folder
                            String doneFolderOfUser = LAStringUtil.buildFolderPath(Constants.DASH + storedJob.getProjectCode(), Constants.DONE, storedJob.getName(), jobTeamUserDTO.getUserLogin());
                            fileSystemHandlingService.deleteDirectory(doneFolderOfUser);
                            fileSystemHandlingService.makeDirectory(doneFolderOfUser);

                            // move files from backlogs into todo folder
                            if (iFile >= files.size()) {
                                jobTeamDTO.setTotalFiles(new Long(Constants.ZERO));
                            } else {

                                if (jobTeamDTO.getTotalFiles() != null) {
                                    Set<JobTeamUserTaskDTO> jobTeamUserTaskDTOs = new HashSet<>();
                                    if(iFile < files.size()) {
                                        for (int iActualTotalFiles = Constants.ONE; iActualTotalFiles <= jobTeamUserDTO.getTotalFiles(); iActualTotalFiles++) {

                                            File file = files.get(iFile);
                                            if (file.isDirectory()) {
                                                continue;
                                            }

//                                          if(iActualTotalFiles > jobTeamUserDTO.getTotalFiles()) {
//                                              break;
//                                          }

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
                                            String newFileName = storedJob.getProjectCode() + Constants.UNDERSCORE + storedJob.getName() + Constants.UNDERSCORE + sequenceDataDao.nextJobTeamUserTaskId() + Constants.UNDERSCORE + remoteFileName;
                                            jobTeamUserTaskDTO.setFileName(newFileName);

                                            jobTeamUserTaskDTOs.add(jobTeamUserTaskDTO);
                                            fileSystemHandlingService.copy(file.getPath().substring(this.applicationProperties.getRootFolder().length()), toDoFolderOfUser, newFileName);

                                            totalFilesForJobTeam++;
                                            iFile++;

                                            if (iFile >= files.size()) {
                                                break;
                                            }
                                        }
                                        jobTeamUserDTO.setTotalFiles(new Long(jobTeamUserTaskDTOs.size()));
                                        jobTeamUserDTO.setJobTeamUserTasks(jobTeamUserTaskDTOs);
                                    }

                                }

                            }
                        }
                    }
                    jobTeamDTO.setTotalFiles(totalFilesForJobTeam);
                }



            }else {

//                jobService.updateDeadLineAndCustomerRequirements(params.getDeadline(), params.getCustomerRequirements(), storedJob.getId());

                // adjust or assign more task to current storedJob team users
                Map<Long, JobTeamUserDTO> jobTeamUsersMap = new HashMap<>();
                for(JobTeamDTO jobTeamDTO: params.getTeams()) {
                    for(JobTeamUserDTO jobTeamUserDTO: jobTeamDTO.getJobTeamUsers()) {
                        Long id = jobTeamUserDTO.getId();
                        if(id != null) {
                            jobTeamUsersMap.put(id, jobTeamUserDTO);
                        }
                    }
                }


                Long newTotalFilesOfAllUsersInJob = 0L;

                List<String> totalAssignedRelativeFilePath = new ArrayList<>();

                for(JobTeamDTO storedJobTeamDTO: storedJob.getJobTeams()) {
                    for(JobTeamUserDTO storedJobTeamUserDTO: storedJobTeamDTO.getJobTeamUsers()) {

                        if(!jobTeamUsersMap.containsKey(storedJobTeamUserDTO.getId())) {
                            throw new CustomParameterizedException("Can not remove user " + storedJobTeamUserDTO.getName() + " when job is started");
                        }

                        JobTeamUserDTO newJobTeamUserDTO = jobTeamUsersMap.get(storedJobTeamUserDTO.getId());
                        if(newJobTeamUserDTO.getTotalFiles() < storedJobTeamUserDTO.getTotalFiles()) {
                            throw new CustomParameterizedException("Can not edit new total files of user " + storedJobTeamUserDTO.getName() + " less than origin");
                        }

                        newTotalFilesOfAllUsersInJob += newJobTeamUserDTO.getTotalFiles();

                        for(JobTeamUserTask assignedTask: jobTeamUserTaskService.findByJobTeamUserIdAndJobId(storedJobTeamUserDTO.getId(), storedJob.getId())) {
                            totalAssignedRelativeFilePath.add(assignedTask.getOriginalFilePath() + Constants.DASH + assignedTask.getOriginalFileName());
                        }

                    }
                }

                if(storedJob.getTotalFiles() < newTotalFilesOfAllUsersInJob) {
                    throw new CustomParameterizedException(newTotalFilesOfAllUsersInJob + " exceed the current total files " + storedJob.getTotalFiles() + " of job " + storedJob.getName());
                }

                // assign more files
                List<String> backLogRelativeFilePath = fileSystemHandlingService.listRelativeFilePathRecursiveFromPath(LAStringUtil.buildFolderPath(storedJob.getProjectCode(),Constants.BACK_LOGS, storedJob.getName()));

                backLogRelativeFilePath.removeAll(totalAssignedRelativeFilePath);
                int iRelativeFilePath = 0;

                for(JobTeamDTO storedJobTeamDTO: storedJob.getJobTeams()) {
                    Long newTotalFilesForTeam = 0L;
                    for(JobTeamUserDTO storedJobTeamUserDTO: storedJobTeamDTO.getJobTeamUsers()) {
                        JobTeamUserDTO newJobTeamUserDTO = jobTeamUsersMap.get(storedJobTeamUserDTO.getId());
                        Long newAssignments = newJobTeamUserDTO.getTotalFiles() - storedJobTeamUserDTO.getTotalFiles();
                        String toDoFolderOfUser = LAStringUtil.buildFolderPath(Constants.DASH + storedJob.getProjectCode(), Constants.TO_DO, storedJob.getName(), storedJobTeamUserDTO.getUserLogin());
                        for(int i = 0; i < newAssignments; i++) {

                            JobTeamUserTaskDTO jobTeamUserTaskDTO = new JobTeamUserTaskDTO();
                            jobTeamUserTaskDTO.setFilePath(toDoFolderOfUser);

                            jobTeamUserTaskDTO.setJobTeamUserId(storedJobTeamUserDTO.getId()); // assignee

                            String relativeFilePath = backLogRelativeFilePath.get(iRelativeFilePath);

                            String originalRelativeFilePath = relativeFilePath.substring(0, relativeFilePath.lastIndexOf(Constants.DASH));

                            String originalFileName = relativeFilePath.substring(relativeFilePath.lastIndexOf(Constants.DASH) + 1); // + 1 for remove DASH

                            jobTeamUserTaskDTO.setOriginalFilePath(originalRelativeFilePath);
                            jobTeamUserTaskDTO.setOriginalFileName(originalFileName);

                            jobTeamUserTaskDTO.setStatus(FileStatusEnum.TODO);

                            // setup file name
                            String newFileName = storedJob.getProjectCode() + Constants.UNDERSCORE + storedJob.getName() + Constants.UNDERSCORE + sequenceDataDao.nextJobTeamUserTaskId() + Constants.UNDERSCORE + originalFileName;
                            jobTeamUserTaskDTO.setFileName(newFileName);

                            fileSystemHandlingService.copy(relativeFilePath, toDoFolderOfUser, newFileName);

                            storedJobTeamUserDTO.addJobTeamUserTask(jobTeamUserTaskDTO);

                            iRelativeFilePath++;
                        }

                        storedJobTeamUserDTO.setTotalFiles(newJobTeamUserDTO.getTotalFiles());

                        newTotalFilesForTeam += newJobTeamUserDTO.getTotalFiles();
                    }

                    storedJobTeamDTO.setTotalFiles(newTotalFilesForTeam);
                }

            }

            // re-update storedJob
            storedJob = jobService.save(storedJob);
        }

        UpdatePlanResponseVM rs = new UpdatePlanResponseVM();
        rs.setJob(storedJob);

        return rs;
    }

    @Override
    @Transactional(readOnly = false)
    public EmptyResponseVM adjust(AdjustFilesParamDTO params) throws Exception{

        List<JobTeamUserTaskDTO> storedAssignments = jobTeamUserTaskService.findJobToDoList(params.getJobTeamUserId(), params.getJobId());
        User toUser = userService.findById(params.getToUserId());
        JobTeamUserDTO storedJobTeamUserDTO = jobTeamUserService.findOne(params.getJobTeamUserId());
        JobTeamDTO storedJobTeamDTO = jobTeamService.findOne(storedJobTeamUserDTO.getJobTeamId());

        JobDTO storedJob = jobService.findOne(params.getJobId());
        if(toUser.getTeam() == null) {
            throw new CustomParameterizedException("User " + toUser.getLastName() + " is not belong to any team");
        }
        if(toUser.getId() == storedJobTeamUserDTO.getUserId()) {
            throw new CustomParameterizedException("Can not assign to owner");
        }

        storedJobTeamDTO.setTotalFiles(storedJobTeamDTO.getTotalFiles() - params.getTotalFilesAdjustment());
        storedJobTeamDTO = jobTeamService.save(storedJobTeamDTO);


        JobTeamDTO jobTeam = jobTeamService.findByJobIdAndTeamId(params.getJobId(), toUser.getTeam().getId());
        JobTeamUserDTO newJobTeamUser = null;
        if(jobTeam != null && jobTeam.getId() == storedJobTeamDTO.getId()) {

            for(JobTeamUserDTO jobTeamUser: storedJobTeamDTO.getJobTeamUsers()) {
                if(jobTeamUser.getUserId() == toUser.getId()) {
                    newJobTeamUser = jobTeamUser;
                    break;
                }
            }

            storedJobTeamDTO.setTotalFiles(jobTeam.getTotalFiles() + params.getTotalFilesAdjustment());

            if(newJobTeamUser == null) {
                newJobTeamUser = new JobTeamUserDTO();
                newJobTeamUser.setJobTeamId(jobTeam.getId());
                newJobTeamUser.setTotalFiles(params.getTotalFilesAdjustment());
                newJobTeamUser.setUserId(toUser.getId());
                storedJobTeamDTO.addJobTeamUser(newJobTeamUser);

                newJobTeamUser = jobTeamUserService.save(newJobTeamUser);

            }else {
                newJobTeamUser.setTotalToDoFiles(newJobTeamUser.getTotalFiles() + params.getTotalFilesAdjustment());
                newJobTeamUser = jobTeamUserService.save(newJobTeamUser);
            }
        }else {
            jobTeam = new JobTeamDTO();
            jobTeam.setJobId(params.getJobId());
            jobTeam.setTeamId(toUser.getTeam().getId());
            jobTeam.setProjectId(storedJob.getProjectId());
            jobTeam.setTotalFiles(params.getTotalFilesAdjustment());
            storedJob.addJobTeam(jobTeam);
            jobTeam = jobTeamService.save(jobTeam);

            TeamDTO teamDTO = teamService.findOne(toUser.getTeam().getId());
            for(UserDTO user: teamDTO.getMembers()) {
                JobTeamUserDTO jobTeamUserDTO = new JobTeamUserDTO();
                jobTeamUserDTO.setJobTeamId(jobTeam.getId());
                if(user.getId() == toUser.getId()) {
                    jobTeamUserDTO.setTotalFiles(params.getTotalFilesAdjustment());
                }else {
                    jobTeamUserDTO.setTotalFiles(0L);
                }
                jobTeamUserDTO.setUserId(user.getId());

                jobTeam.addJobTeamUser(jobTeamUserDTO);
                jobTeamUserDTO = jobTeamUserService.save(jobTeamUserDTO);

                if(user.getId() == toUser.getId()) {
                    newJobTeamUser = jobTeamUserDTO;
                }
            }
        }

        // To-do folder
        String toDoFolderOfUser = LAStringUtil.buildFolderPath(Constants.DASH + storedJob.getProjectCode(), Constants.TO_DO, storedJob.getName(), toUser.getLogin());
        if(!fileSystemHandlingService.checkFolderExist(toDoFolderOfUser)) {
            fileSystemHandlingService.makeDirectory(toDoFolderOfUser);
        }


        // to-check folder
        String toCheckFolderOfUser = LAStringUtil.buildFolderPath(Constants.DASH + storedJob.getProjectCode(), Constants.TO_CHECK, storedJob.getName(), toUser.getLogin());
        if(!fileSystemHandlingService.checkFolderExist(toCheckFolderOfUser)) {
            fileSystemHandlingService.makeDirectory(toCheckFolderOfUser);
        }

        // done folder
        String doneFolderOfUser = LAStringUtil.buildFolderPath(Constants.DASH + storedJob.getProjectCode(), Constants.DONE, storedJob.getName(), toUser.getLogin());
        if(!fileSystemHandlingService.checkFolderExist(doneFolderOfUser)) {
            fileSystemHandlingService.makeDirectory(doneFolderOfUser);
        }

        for(int i = 0; i < params.getTotalFilesAdjustment(); i++) {
            JobTeamUserTaskDTO storedJobTeamUserTaskDTO = storedAssignments.get(i);

            storedJobTeamUserDTO.removeJobTeamUserTask(storedJobTeamUserTaskDTO);

            JobTeamUserTaskDTO newJobTeamUserTaskDTO = new JobTeamUserTaskDTO();
            newJobTeamUserTaskDTO.setFilePath(toDoFolderOfUser);

            newJobTeamUserTaskDTO.setJobTeamUserId(newJobTeamUser.getId()); // assignee


            String originalRelativeFilePath = storedJobTeamUserTaskDTO.getOriginalFilePath();

            String originalFileName = storedJobTeamUserTaskDTO.getOriginalFileName();

            newJobTeamUserTaskDTO.setOriginalFilePath(originalRelativeFilePath);
            newJobTeamUserTaskDTO.setOriginalFileName(originalFileName);

            newJobTeamUserTaskDTO.setStatus(FileStatusEnum.TODO);

            // setup file name
            String newFileName = storedJob.getProjectCode() + Constants.UNDERSCORE + storedJob.getName() + Constants.UNDERSCORE + sequenceDataDao.nextJobTeamUserTaskId() + Constants.UNDERSCORE + originalFileName;
            newJobTeamUserTaskDTO.setFileName(newFileName);

            fileSystemHandlingService.copy(originalRelativeFilePath + Constants.DASH + originalFileName, toDoFolderOfUser, newFileName);

            newJobTeamUser.addJobTeamUserTask(newJobTeamUserTaskDTO);

            newJobTeamUserTaskDTO = jobTeamUserTaskService.save(newJobTeamUserTaskDTO);


            // remove old file on disk
            String oldFilePath = storedJobTeamUserTaskDTO.getFilePath() + Constants.DASH + storedJobTeamUserTaskDTO.getFileName();
            fileSystemHandlingService.deleteFile(oldFilePath);
        }

        storedJobTeamUserDTO.setTotalFiles(storedJobTeamUserDTO.getTotalFiles() - params.getTotalFilesAdjustment());
        storedJobTeamUserDTO = jobTeamUserService.save(storedJobTeamUserDTO);

//        newJobTeamUser = jobTeamUserService.save(newJobTeamUser);

        cacheManager.getCacheNames().parallelStream().forEach(name -> cacheManager.getCache(name).clear());


        EmptyResponseVM rs = new EmptyResponseVM();
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
                            if(row[0] != null) {
                                jobTeamUserDTO.setTotalToDoFiles(Long.parseLong(row[0].toString()));
                            }
                            if(row[1] != null) {
                                jobTeamUserDTO.setTotalToCheckFiles(Long.parseLong(row[1].toString()));
                            }
                            if(row[2] != null) {
                                jobTeamUserDTO.setTotalDoneFiles(Long.parseLong(row[2].toString()));
                            }
                            if(row[3] != null) {
                                jobTeamUserDTO.setTotalDeliveryFiles(Long.parseLong(row[3].toString()));
                            }

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

    @Override
    public UserJobDetailResponseVM getUserJobDetail(GetUserJobDetailParamDTO params) {
        UserJobDetailResponseVM rs = new UserJobDetailResponseVM();
        JobTeamUserDTO jobTeamUserDTO = jobTeamUserService.findOne(params.getJobTeamUserId());
        jobTeamUserDTO.setTotalToDoFiles(jobTeamUserTaskService.countJobToDoList(params.getJobTeamUserId(), params.getJobId()));

        JobDTO jobDTO = jobService.findOne(params.getJobId());

        rs.setJob(jobDTO);
        rs.setJobTeamUser(jobTeamUserDTO);

        return rs;
    }
}

