package vn.com.la.service.impl;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.la.config.ApplicationProperties;
import vn.com.la.config.Constants;
import vn.com.la.domain.JobTeam;
import vn.com.la.domain.JobTeamUser;
import vn.com.la.domain.JobTeamUserTask;
import vn.com.la.domain.enumeration.FileStatusEnum;
import vn.com.la.repository.SequenceDataDao;
import vn.com.la.service.*;
import vn.com.la.service.dto.*;
import vn.com.la.service.dto.param.GetJobPlanDetailParamDTO;
import vn.com.la.service.dto.param.UpdatePlanParamDTO;
import vn.com.la.service.util.LAStringUtil;
import vn.com.la.web.rest.errors.CustomParameterizedException;
import vn.com.la.web.rest.vm.response.JobPlanDetailResponseVM;
import vn.com.la.web.rest.vm.response.UpdatePlanResponseVM;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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


    public PlanServiceImpl(JobService jobService, JobTeamService jobTeamService, FileSystemHandlingService ftpService,
                           JobTeamUserTaskService jobTeamUserTaskService,
                           SequenceDataDao sequenceDataDao, JobTeamUserService jobTeamUserService,
                           ProjectService projectService, ApplicationProperties applicationProperties) {
        this.jobService = jobService;
        this.jobTeamService = jobTeamService;
        this.fileSystemHandlingService = ftpService;
        this.jobTeamUserTaskService = jobTeamUserTaskService;
        this.sequenceDataDao = sequenceDataDao;
        this.jobTeamUserService = jobTeamUserService;
        this.projectService = projectService;
        this.applicationProperties = applicationProperties;
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
                                        String originalRelativeFilePath = filePath.substring(filePath.indexOf(backLogItemPath));

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
    public List<ProjectDTO> getAllPlans() {

        List<ProjectDTO> projectDTOs = projectService.findAll();
        List<Long> userIds = new ArrayList<>();
        for(ProjectDTO projectDTO: projectDTOs) {
            for(JobDTO jobDTO: projectDTO.getJobs()) {
                for(JobTeamDTO jobTeamDTO: jobDTO.getJobTeams()) {
                    for(JobTeamUserDTO jobTeamUserDTO: jobTeamDTO.getJobTeamUsers()) {
                        userIds.add(jobTeamUserDTO.getUserId());
                    }
                }
            }
        }



        return null;
    }
}
