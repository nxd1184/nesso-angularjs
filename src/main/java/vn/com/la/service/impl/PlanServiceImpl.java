package vn.com.la.service.impl;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.la.config.ApplicationProperties;
import vn.com.la.config.Constants;
import vn.com.la.domain.*;
import vn.com.la.domain.enumeration.FileStatusEnum;
import vn.com.la.domain.enumeration.ProjectStatusEnum;
import vn.com.la.repository.SequenceDataDao;
import vn.com.la.service.*;
import vn.com.la.service.dto.*;
import vn.com.la.service.dto.param.*;
import vn.com.la.service.util.LADateTimeUtil;
import vn.com.la.service.util.LAStringUtil;
import vn.com.la.web.rest.errors.CustomParameterizedException;
import vn.com.la.web.rest.vm.response.*;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.File;
import java.time.ZonedDateTime;
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
    private final IgnoreNameService ignoreNameService;


    public PlanServiceImpl(JobService jobService, JobTeamService jobTeamService, FileSystemHandlingService ftpService,
                           JobTeamUserTaskService jobTeamUserTaskService,
                           SequenceDataDao sequenceDataDao, JobTeamUserService jobTeamUserService,
                           ProjectService projectService, ApplicationProperties applicationProperties,
                           EntityManager em, UserService userService, TeamService teamService,
                           CacheManager cacheManager, IgnoreNameService ignoreNameService) {
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
        this.ignoreNameService = ignoreNameService;
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
        storedJob.setType(params.getType());
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
                }else {
                    String jobPath = LAStringUtil.buildFolderPath(Constants.SLASH + storedJob.getProjectCode(), Constants.TO_DO, storedJob.getName());
                    for(int i = 2; i <= storedJob.getSequenceTask(); i++) {
                        String jobSequenceName = jobPath + "_" + i;
                        JobDTO storedSequenceJob = jobService.findByNameAndProjectCode(storedJob.getName() + "i", storedJob.getProjectCode());
                        if(BooleanUtils.isNotTrue(storedSequenceJob.getStarted())) {
                            fileSystemHandlingService.deleteDirectory(jobSequenceName);
                            fileSystemHandlingService.makeDirectory(jobSequenceName);
                        }else {
                            throw new CustomParameterizedException("The next sequence job was started, you can not edit the previous sequence job");
                        }

                    }
                }

                storedJob = jobService.save(storedJob);

                // create backlog item folder inside todo, tocheck, done
                String toDoFolder = LAStringUtil.buildFolderPath(Constants.SLASH + storedJob.getProjectCode(), Constants.TO_DO, storedJob.getName());
                fileSystemHandlingService.deleteDirectory(toDoFolder);
                fileSystemHandlingService.makeDirectory(toDoFolder);

                String toCheckFolder = LAStringUtil.buildFolderPath(Constants.SLASH + storedJob.getProjectCode(), Constants.TO_CHECK, storedJob.getName());
                fileSystemHandlingService.deleteDirectory(toCheckFolder);
                fileSystemHandlingService.makeDirectory(toCheckFolder);

                String doneFolder = LAStringUtil.buildFolderPath(Constants.SLASH + storedJob.getProjectCode(), Constants.DONE, storedJob.getName());
                fileSystemHandlingService.deleteDirectory(doneFolder);
                fileSystemHandlingService.makeDirectory(doneFolder);

                String deliveryFolder = LAStringUtil.buildFolderPath(Constants.SLASH + storedJob.getProjectCode(), Constants.DELIVERY, storedJob.getName());
                fileSystemHandlingService.deleteDirectory(deliveryFolder);
                fileSystemHandlingService.makeDirectory(deliveryFolder);
                // move files from backlogs to to-do

                long totalFilesInBacklogItem = storedJob.getTotalFiles();
                // list file from backlog item
                String backLogItemPath = LAStringUtil.buildFolderPath(Constants.SLASH + storedJob.getProjectCode(), Constants.BACK_LOGS, storedJob.getName());
                List<File> files = fileSystemHandlingService.listFileRecursiveFromPath(backLogItemPath, ignoreNameService.findAll());
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
                            String toDoFolderOfUser = LAStringUtil.buildFolderPath(Constants.SLASH + storedJob.getProjectCode(), Constants.TO_DO, storedJob.getName(), jobTeamUserDTO.getUserLogin());
                            fileSystemHandlingService.deleteDirectory(toDoFolderOfUser);
                            fileSystemHandlingService.makeDirectory(toDoFolderOfUser);

                            // to-check folder
                            String toCheckFolderOfUser = LAStringUtil.buildFolderPath(Constants.SLASH + storedJob.getProjectCode(), Constants.TO_CHECK, storedJob.getName(), jobTeamUserDTO.getUserLogin());
                            fileSystemHandlingService.deleteDirectory(toCheckFolderOfUser);
                            fileSystemHandlingService.makeDirectory(toCheckFolderOfUser);

                            // done folder
                            String doneFolderOfUser = LAStringUtil.buildFolderPath(Constants.SLASH + storedJob.getProjectCode(), Constants.DONE, storedJob.getName(), jobTeamUserDTO.getUserLogin());
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
                                            String newFileName = sequenceDataDao.nextJobTeamUserTaskId()  + Constants.UNDERSCORE + storedJob.getName() + originalRelativeFilePath.replaceFirst(storedJob.getProjectCode() + Constants.SLASH + Constants.BACK_LOGS + Constants.SLASH + storedJob.getName(), "").replace(Constants.SLASH, Constants.UNDERSCORE) + Constants.UNDERSCORE + remoteFileName;
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
                            totalAssignedRelativeFilePath.add(assignedTask.getOriginalFilePath() + Constants.SLASH + assignedTask.getOriginalFileName());
                        }

                    }
                }

                if(storedJob.getTotalFiles() < newTotalFilesOfAllUsersInJob) {
                    throw new CustomParameterizedException(newTotalFilesOfAllUsersInJob + " exceed the current total files " + storedJob.getTotalFiles() + " of job " + storedJob.getName());
                }

                // assign more files
                List<String> backLogRelativeFilePath = fileSystemHandlingService.listRelativeFilePathRecursiveFromPath(LAStringUtil.buildFolderPath(storedJob.getProjectCode(),Constants.BACK_LOGS, storedJob.getName()), ignoreNameService.findAll());

                backLogRelativeFilePath.removeAll(totalAssignedRelativeFilePath);
                int iRelativeFilePath = 0;

                for(JobTeamDTO storedJobTeamDTO: storedJob.getJobTeams()) {
                    Long newTotalFilesForTeam = 0L;
                    for(JobTeamUserDTO storedJobTeamUserDTO: storedJobTeamDTO.getJobTeamUsers()) {
                        JobTeamUserDTO newJobTeamUserDTO = jobTeamUsersMap.get(storedJobTeamUserDTO.getId());
                        Long newAssignments = newJobTeamUserDTO.getTotalFiles() - storedJobTeamUserDTO.getTotalFiles();
                        String toDoFolderOfUser = LAStringUtil.buildFolderPath(Constants.SLASH + storedJob.getProjectCode(), Constants.TO_DO, storedJob.getName(), storedJobTeamUserDTO.getUserLogin());
                        for(int i = 0; i < newAssignments; i++) {

                            JobTeamUserTaskDTO jobTeamUserTaskDTO = new JobTeamUserTaskDTO();
                            jobTeamUserTaskDTO.setFilePath(toDoFolderOfUser);

                            jobTeamUserTaskDTO.setJobTeamUserId(storedJobTeamUserDTO.getId()); // assignee

                            String relativeFilePath = backLogRelativeFilePath.get(iRelativeFilePath);

                            String originalRelativeFilePath = relativeFilePath.substring(0, relativeFilePath.lastIndexOf(Constants.SLASH));

                            String originalFileName = relativeFilePath.substring(relativeFilePath.lastIndexOf(Constants.SLASH) + 1); // + 1 for remove SLASH

                            jobTeamUserTaskDTO.setOriginalFilePath(originalRelativeFilePath);
                            jobTeamUserTaskDTO.setOriginalFileName(originalFileName);

                            jobTeamUserTaskDTO.setStatus(FileStatusEnum.TODO);

                            // setup file name
                            String newFileName = sequenceDataDao.nextJobTeamUserTaskId() + originalRelativeFilePath.replaceFirst(Constants.SLASH + storedJob.getProjectCode() + Constants.SLASH + Constants.BACK_LOGS + Constants.SLASH + storedJob.getName(), "").replace(Constants.SLASH, Constants.UNDERSCORE) + Constants.UNDERSCORE  + originalFileName;
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

            // re-updateByProjectViewAndStatusType storedJob
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
        if(jobTeam != null) {

            if(jobTeam.getId() == storedJobTeamDTO.getId()) {
                // assign to same team
                for(JobTeamUserDTO jobTeamUser: storedJobTeamDTO.getJobTeamUsers()) {
                    if(jobTeamUser.getUserId() == toUser.getId()) {
                        newJobTeamUser = jobTeamUser;
                        break;
                    }
                }

                storedJobTeamDTO.setTotalFiles(jobTeam.getTotalFiles() + params.getTotalFilesAdjustment());
                storedJobTeamDTO = jobTeamService.save(storedJobTeamDTO);

                if(newJobTeamUser == null) {
                    newJobTeamUser = new JobTeamUserDTO();
                    newJobTeamUser.setJobTeamId(storedJobTeamDTO.getId());
                    newJobTeamUser.setTotalFiles(params.getTotalFilesAdjustment());
                    newJobTeamUser.setUserId(toUser.getId());
                    storedJobTeamDTO.addJobTeamUser(newJobTeamUser);

                    newJobTeamUser = jobTeamUserService.save(newJobTeamUser);

                }else {
                    newJobTeamUser.setTotalFiles(newJobTeamUser.getTotalFiles() + params.getTotalFilesAdjustment());
                    newJobTeamUser = jobTeamUserService.save(newJobTeamUser);
                }
            }else {

                // assign to other team
                for(JobTeamUserDTO jobTeamUser: jobTeam.getJobTeamUsers()) {
                    if(jobTeamUser.getUserId() == toUser.getId()) {
                        newJobTeamUser = jobTeamUser;
                        break;
                    }
                }

                jobTeam.setTotalFiles(jobTeam.getTotalFiles() + params.getTotalFilesAdjustment());
                jobTeam = jobTeamService.save(jobTeam);

                if(newJobTeamUser == null) {
                    newJobTeamUser = new JobTeamUserDTO();
                    newJobTeamUser.setJobTeamId(jobTeam.getId());
                    newJobTeamUser.setTotalFiles(params.getTotalFilesAdjustment());
                    newJobTeamUser.setUserId(toUser.getId());
                    jobTeam.addJobTeamUser(newJobTeamUser);

                    newJobTeamUser = jobTeamUserService.save(newJobTeamUser);

                }else {
                    newJobTeamUser.setTotalFiles(newJobTeamUser.getTotalFiles() + params.getTotalFilesAdjustment());
                    newJobTeamUser = jobTeamUserService.save(newJobTeamUser);
                }

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
        String toDoFolderOfUser = LAStringUtil.buildFolderPath(Constants.SLASH + storedJob.getProjectCode(), Constants.TO_DO, storedJob.getName(), toUser.getLogin());
        if(!fileSystemHandlingService.checkFolderExist(toDoFolderOfUser)) {
            fileSystemHandlingService.makeDirectory(toDoFolderOfUser);
        }


        // to-check folder
        String toCheckFolderOfUser = LAStringUtil.buildFolderPath(Constants.SLASH + storedJob.getProjectCode(), Constants.TO_CHECK, storedJob.getName(), toUser.getLogin());
        if(!fileSystemHandlingService.checkFolderExist(toCheckFolderOfUser)) {
            fileSystemHandlingService.makeDirectory(toCheckFolderOfUser);
        }

        // done folder
        String doneFolderOfUser = LAStringUtil.buildFolderPath(Constants.SLASH + storedJob.getProjectCode(), Constants.DONE, storedJob.getName(), toUser.getLogin());
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
            String newFileName = sequenceDataDao.nextJobTeamUserTaskId() + originalRelativeFilePath.replaceFirst(Constants.SLASH + storedJob.getProjectCode() + Constants.SLASH + Constants.BACK_LOGS + Constants.SLASH + storedJob.getName(), "").replace(Constants.SLASH, Constants.UNDERSCORE) + Constants.UNDERSCORE + originalFileName;
            newJobTeamUserTaskDTO.setFileName(newFileName);

            fileSystemHandlingService.copy(originalRelativeFilePath + Constants.SLASH + originalFileName, toDoFolderOfUser, newFileName);

            newJobTeamUser.addJobTeamUserTask(newJobTeamUserTaskDTO);

            newJobTeamUserTaskDTO = jobTeamUserTaskService.save(newJobTeamUserTaskDTO);


            // remove old file on disk
            String oldFilePath = storedJobTeamUserTaskDTO.getFilePath() + Constants.SLASH + storedJobTeamUserTaskDTO.getFileName();
            fileSystemHandlingService.deleteFile(oldFilePath);
        }

        storedJobTeamUserDTO.setTotalFiles(storedJobTeamUserDTO.getTotalFiles() - params.getTotalFilesAdjustment());
        storedJobTeamUserDTO = jobTeamUserService.save(storedJobTeamUserDTO);

//        newJobTeamUser = jobTeamUserService.save(newJobTeamUser);

        cacheManager.getCacheNames().parallelStream().forEach(name -> cacheManager.getCache(name).clear());
//        cacheManager.getCache(Project.class.getName()).evict(storedJob.getProjectId());

        EmptyResponseVM rs = new EmptyResponseVM();
        return rs;
    }

    @Override
    public GetAllPlanResponseVM getAllPlans(GetAllPlanParamDTO params) {

        GetAllPlanResponseVM rs = new GetAllPlanResponseVM();

        if(params.getType() == PlanTypeEnumDTO.STATUS) {
            if(PlanViewEnumDTO.PROJECT == params.getView()) {
                rs.setProjects(buildPlansByProjectForStatus(params.getProjectCode(), params.getTaskCode()));
            }else if(PlanViewEnumDTO.USER == params.getView()) {
                rs.setTeams(buildPlansByTeamsForStatus(params.getProjectCode(), params.getTaskCode()));
            }
        }else if(params.getType() == PlanTypeEnumDTO.TIMELINE) {
            if(PlanViewEnumDTO.PROJECT == params.getView()) {
                rs.setTimelineProjects(buildPlansByProjectForTimeline(params.getFromDate(), params.getToDate(), params.getProjectCode(), params.getTaskCode()));
            }else if(PlanViewEnumDTO.USER == params.getView()) {
                rs.setTeams(buildPlansByTeamsForTimeline(params.getFromDate(), params.getToDate(), params.getProjectCode(), params.getTaskCode()));
            }
        }

        return rs;
    }

    private List<ProjectDTO> buildPlansByProjectForStatus(String projectCode, String taskCode) {

        SearchProjectParamDTO criteria = new SearchProjectParamDTO();
        criteria.setProjectCode(projectCode);
        criteria.setTaskCode(taskCode);
        criteria.setStatus(ProjectStatusEnum.ACTIVE);

        Pageable pagable = new PageRequest(0,20, Sort.Direction.DESC, "createdDate");
        Page<ProjectDTO> page = projectService.search(criteria, pagable);

        List<ProjectDTO> projectDTOs = page.getContent();

        // bad implementation
        // remove finished job
        for(ProjectDTO projectDTO: projectDTOs) {
            projectDTO.getJobs().removeIf(jobDTO -> jobDTO.getFinishDate() != null);
        }

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

    private Map<Long, PlanTeamDTO> buildPlansByTeamsForStatus(String projectCode, String taskCode) {

        Map<Long, PlanTeamDTO> teams = new HashMap<>();

        String conditions = "p.status = 'ACTIVE'";
        if(StringUtils.isNotBlank(projectCode)) {
            conditions = " AND p.code like '%" + projectCode + "%'";
        }

        if(StringUtils.isNotBlank(taskCode)) {
            if(StringUtils.isNotBlank(conditions)) {
                conditions += " AND ";
            }else {
                conditions = "";
            }

            conditions += " t.code like '%" + taskCode + "%'";
        }

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
        sqlBuilder.append(" left join job_task job_task on job_task.job_id = j.id");
        sqlBuilder.append(" left join task task on job_task.task_id = task.id");
        sqlBuilder.append(" inner join project p on p.id = j.project_id");
        sqlBuilder.append(" inner join job_team_user jtu on jtu.job_team_id = jt.id");
        sqlBuilder.append(" inner join jhi_user jhi_user on jhi_user.id = jtu.user_id");
        sqlBuilder.append(" inner join job_team_user_task jtut on jtut.job_team_user_id = jtu.id");
        if(StringUtils.isNotBlank(conditions)) {
            sqlBuilder.append(" WHERE ").append(conditions);
        }
        sqlBuilder.append(" group by jt.team_id, t.name, jtu.user_id, jhi_user.last_name, p.id, p.name, j.id, j.name, jtu.total_files");
        sqlBuilder.append(" ORDER BY p.created_date desc");
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
            team.updateByProjectViewAndStatusType(row);
        }

        return teams;
    }


    private Map<Long, PlanProjectDTO> buildPlansByProjectForTimeline(ZonedDateTime fromDate, ZonedDateTime toDate, String projectCode, String taskCode) {
        Map<Long, PlanProjectDTO> projects = new HashMap<>();

        String conditions = "p.status = 'ACTIVE'";
        if(StringUtils.isNotBlank(projectCode)) {
            conditions = " AND p.code like '%" + projectCode + "%'";
        }

        if(StringUtils.isNotBlank(taskCode)) {
            if(StringUtils.isNotBlank(conditions)) {
                conditions += " AND ";
            }else {
                conditions = "";
            }

            conditions += " t.code like '%" + taskCode + "%'";
        }

        String betweenCondition = "BETWEEN '" + LADateTimeUtil.toJodaDateTime(fromDate).toString(LADateTimeUtil.DATETIME_FORMAT) + "' AND '" + LADateTimeUtil.toJodaDateTime(toDate).toString(LADateTimeUtil.DATETIME_FORMAT) + "'";

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT p.id as project_id, p.name as project_name, j.id as job_id, j.name as job_name, t.id as team_id, t.name as team_name, jhi_user.id as user_id, jhi_user.last_name as user_name, ");
//        sqlBuilder.append("     sum(case when jtut.status IN ('TODO','REWORK') and (jtut.created_date " + betweenCondition + " or jtut.last_rework_time " + betweenCondition + ") then 1 else 0 end) as TOTAL_TODO, ");
//        sqlBuilder.append("     sum(case when jtut.status = 'DONE' and jtut.last_done_time " + betweenCondition + " then 1 else 0 end) as TOTAL_DONE,");

        DateTime fromDateDT = LADateTimeUtil.toJodaDateTime(fromDate).withTimeAtStartOfDay();
        DateTime toDateDT = LADateTimeUtil.toJodaDateTime(toDate).withTimeAtStartOfDay();

        betweenCondition = "BETWEEN '" + fromDateDT.toString(LADateTimeUtil.DATETIME_FORMAT) + "' AND '" + LADateTimeUtil.toMidnightOfDate(fromDateDT).toString(LADateTimeUtil.DATETIME_FORMAT) + "'";

        String dayOfWeek = fromDateDT.toString("E_d");
        sqlBuilder.append("     sum(case when jtut.status = 'DONE' and jtut.last_done_time " + betweenCondition + " then 1 else 0 end) as " + dayOfWeek + ",");

        fromDateDT = fromDateDT.plusDays(1);
        while(fromDateDT.isBefore(toDateDT)) {

            betweenCondition = "BETWEEN '" + fromDateDT.toString(LADateTimeUtil.DATETIME_FORMAT) + "' AND '" + LADateTimeUtil.toMidnightOfDate(fromDateDT).toString(LADateTimeUtil.DATETIME_FORMAT) + "'";

            dayOfWeek = fromDateDT.toString("E_d");
            sqlBuilder.append("     sum(case when jtut.status = 'DONE' and jtut.last_done_time " + betweenCondition + " then 1 else 0 end) as " + dayOfWeek + ",");

            fromDateDT = fromDateDT.plusDays(1);
        }

        betweenCondition = "BETWEEN '" + toDateDT.toString(LADateTimeUtil.DATETIME_FORMAT) + "' AND '" + LADateTimeUtil.toMidnightOfDate(toDateDT).toString(LADateTimeUtil.DATETIME_FORMAT) + "'";

        dayOfWeek = fromDateDT.toString("E_d");
        sqlBuilder.append("     sum(case when jtut.status = 'DONE' and jtut.last_done_time " + betweenCondition + " then 1 else 0 end) as " + dayOfWeek);

        sqlBuilder.append(" from job_team jt");
        sqlBuilder.append(" inner join team t on jt.team_id = t.id");
        sqlBuilder.append(" inner join job j on j.id = jt.job_id");
        sqlBuilder.append(" left join job_task job_task on job_task.job_id = j.id");
        sqlBuilder.append(" left join task task on job_task.task_id = task.id");
        sqlBuilder.append(" inner join project p on p.id = j.project_id");
        sqlBuilder.append(" inner join job_team_user jtu on jtu.job_team_id = jt.id");
        sqlBuilder.append(" inner join jhi_user jhi_user on jhi_user.id = jtu.user_id");
        sqlBuilder.append(" inner join job_team_user_task jtut on jtut.job_team_user_id = jtu.id");
        if(StringUtils.isNotBlank(conditions)) {
            sqlBuilder.append(" WHERE ").append(conditions);
        }
        sqlBuilder.append(" group by p.id, p.name, j.id, j.name, t.id, t.name, jhi_user.id, jhi_user.last_name, jtu.total_files");
        sqlBuilder.append(" ORDER BY p.created_date desc");
        Query query = em.createNativeQuery(sqlBuilder.toString());

        List<Object[]> rows = query.getResultList();

        for(Object[] row: rows) {
            PlanProjectDTO project = null;
            Long projectId = Long.parseLong(row[0].toString());
            if(projects.containsKey(projectId)) {
                project = projects.get(projectId);
            }else {
                project = new PlanProjectDTO();
                project.setProjectId(projectId);
                if(row[1] != null) {
                    project.setProjectName(row[1].toString());
                }
                projects.put(projectId, project);
            }
            project.updateByProjectViewAndTimelineType(row);
        }

        return projects;
    }

    private Map<Long, PlanTeamDTO> buildPlansByTeamsForTimeline(ZonedDateTime fromDate, ZonedDateTime toDate, String projectCode, String taskCode) {

        Map<Long, PlanTeamDTO> teams = new HashMap<>();

        String conditions = "p.status = 'ACTIVE'";
        if(StringUtils.isNotBlank(projectCode)) {
            conditions = " AND p.code like '%" + projectCode + "%'";
        }

        if(StringUtils.isNotBlank(taskCode)) {
            if(StringUtils.isNotBlank(conditions)) {
                conditions += " AND ";
            }else {
                conditions = "";
            }

            conditions += " t.code like '%" + taskCode + "%'";
        }

        String betweenCondition = "BETWEEN '" + LADateTimeUtil.toJodaDateTime(fromDate).toString(LADateTimeUtil.DATETIME_FORMAT) + "' AND '" + LADateTimeUtil.toJodaDateTime(toDate).toString(LADateTimeUtil.DATETIME_FORMAT) + "'";

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT jt.team_id as team_id, t.name as team_name,  jtu.user_id as user_id, jhi_user.last_name, p.id as project_id, p.name as project_name, j.id as job_id, j.name as job_name, ");

        DateTime fromDateDT = LADateTimeUtil.toJodaDateTime(fromDate).withTimeAtStartOfDay();
        DateTime toDateDT = LADateTimeUtil.toJodaDateTime(toDate).withTimeAtStartOfDay();

        betweenCondition = "BETWEEN '" + fromDateDT.toString(LADateTimeUtil.DATETIME_FORMAT) + "' AND '" + LADateTimeUtil.toMidnightOfDate(fromDateDT).toString(LADateTimeUtil.DATETIME_FORMAT) + "'";

        String dayOfWeek = fromDateDT.toString("E_d");
        sqlBuilder.append("     sum(case when jtut.status = 'DONE' and jtut.last_done_time " + betweenCondition + " then 1 else 0 end) as " + dayOfWeek + ",");

        fromDateDT = fromDateDT.plusDays(1);
        while(fromDateDT.isBefore(toDateDT)) {

            betweenCondition = "BETWEEN '" + fromDateDT.toString(LADateTimeUtil.DATETIME_FORMAT) + "' AND '" + LADateTimeUtil.toMidnightOfDate(fromDateDT).toString(LADateTimeUtil.DATETIME_FORMAT) + "'";

            dayOfWeek = fromDateDT.toString("E_d");
            sqlBuilder.append("     sum(case when jtut.status = 'DONE' and jtut.last_done_time " + betweenCondition + " then 1 else 0 end) as " + dayOfWeek + ",");

            fromDateDT = fromDateDT.plusDays(1);
        }

        betweenCondition = "BETWEEN '" + toDateDT.toString(LADateTimeUtil.DATETIME_FORMAT) + "' AND '" + LADateTimeUtil.toMidnightOfDate(toDateDT).toString(LADateTimeUtil.DATETIME_FORMAT) + "'";

        dayOfWeek = fromDateDT.toString("E_d");
        sqlBuilder.append("     sum(case when jtut.status = 'DONE' and jtut.last_done_time " + betweenCondition + " then 1 else 0 end) as " + dayOfWeek);

        sqlBuilder.append(" from job_team jt");
        sqlBuilder.append(" inner join team t on jt.team_id = t.id");
        sqlBuilder.append(" inner join job j on j.id = jt.job_id");
        sqlBuilder.append(" left join job_task job_task on job_task.job_id = j.id");
        sqlBuilder.append(" left join task task on job_task.task_id = task.id");
        sqlBuilder.append(" inner join project p on p.id = j.project_id");
        sqlBuilder.append(" inner join job_team_user jtu on jtu.job_team_id = jt.id");
        sqlBuilder.append(" inner join jhi_user jhi_user on jhi_user.id = jtu.user_id");
        sqlBuilder.append(" inner join job_team_user_task jtut on jtut.job_team_user_id = jtu.id");
        if(StringUtils.isNotBlank(conditions)) {
            sqlBuilder.append(" WHERE ").append(conditions);
        }
        sqlBuilder.append(" group by p.id, p.name, j.id, j.name, t.id, t.name, jhi_user.id, jhi_user.last_name, jtu.total_files");
        sqlBuilder.append(" ORDER BY p.created_date desc");
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
            team.updateByUserViewAndTimelineType(row);
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

    @Override
    @Transactional(readOnly = false)
    public FinishJobResponseVM finish(FinishJobParamDTO params) throws Exception {
        FinishJobResponseVM rs = new FinishJobResponseVM();
        if(params.getJobId() == null) {
            rs.setSuccess(false);
        }else {
            JobDTO jobDTO = jobService.findOne(params.getJobId());
            if(jobDTO != null) {
                Long totalUnDoneTasks = jobTeamUserTaskService.countNotDoneTask(jobDTO.getId());
                rs.setTotalUnDoneTasks(totalUnDoneTasks);
                if(totalUnDoneTasks == 0) {
                    jobDTO.setFinishDate(DateTime.now().toDate());
                    jobDTO = jobService.save(jobDTO);
                }else {
                    rs.setSuccess(false);
                }
            }
        }
        return rs;
    }
}

