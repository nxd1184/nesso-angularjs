package vn.com.la.service.impl;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.la.config.Constants;
import vn.com.la.domain.JobTeamUserTask;
import vn.com.la.domain.enumeration.FileStatusEnum;
import vn.com.la.repository.SequenceDataDao;
import vn.com.la.service.*;
import vn.com.la.service.dto.JobDTO;
import vn.com.la.service.dto.JobTeamDTO;
import vn.com.la.service.dto.JobTeamUserDTO;
import vn.com.la.service.dto.JobTeamUserTaskDTO;
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

    public PlanServiceImpl(JobService jobService, JobTeamService jobTeamService, FileSystemHandlingService ftpService,
                           JobTeamUserTaskService jobTeamUserTaskService,
                           SequenceDataDao sequenceDataDao) {
        this.jobService = jobService;
        this.jobTeamService = jobTeamService;
        this.fileSystemHandlingService = ftpService;
        this.jobTeamUserTaskService = jobTeamUserTaskService;
        this.sequenceDataDao = sequenceDataDao;
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

            if(BooleanUtils.isTrue(job.getStarted())) {
                throw new CustomParameterizedException("This job was stared. You can not update it");
            }

            job.setDeadline(params.getDeadline());
            job.setCustomerRequirements(params.getCustomerRequirements());
            job.setJobTasks(params.getTasks());
            job.setJobTeams(params.getTeams());
            job.setTotalFiles(params.getTotalFiles());
            if (job.getSequenceTask() == null) {
                job.setSequenceTask(params.getSequenceTask());
                job.setSequence(Constants.ONE);
            }

            jobService.save(job);

            // create backlog item folder inside todo, tocheck, done
            fileSystemHandlingService.makeDirectory(LAStringUtil.buildFolderPath(Constants.DASH + job.getProjectCode(), Constants.TO_DO, job.getName()));
            fileSystemHandlingService.makeDirectory(LAStringUtil.buildFolderPath(Constants.DASH + job.getProjectCode(), Constants.TO_CHECK, job.getName()));
            fileSystemHandlingService.makeDirectory(LAStringUtil.buildFolderPath(Constants.DASH + job.getProjectCode(), Constants.DONE, job.getName()));
            // move files from backlogs to to-do

            long totalFilesInBacklogItem = job.getTotalFiles();
            // list file from backlog item
            String backLogItemPath = LAStringUtil.buildFolderPath(Constants.DASH + job.getProjectCode(), Constants.BACK_LOGS, job.getName());
            List<File> files = fileSystemHandlingService.listFileFromPath(backLogItemPath);
            int iFile = 0;

            Set<JobTeamDTO> jobTeamDTOs = job.getJobTeams();
            for (JobTeamDTO jobTeamDTO : jobTeamDTOs) {
                long totalFilesForJobTeam = 0;
                if (jobTeamDTO.getJobTeamUsers() != null) {

                    // create user folder (as login name) into todo, tocheck, done of backlog item
                    for (JobTeamUserDTO jobTeamUserDTO : jobTeamDTO.getJobTeamUsers()) {
                        // To-do folder
                        String toDoFolderOfUser = LAStringUtil.buildFolderPath(Constants.DASH + job.getProjectCode(), Constants.TO_DO, job.getName(), jobTeamUserDTO.getUserLogin());
                        fileSystemHandlingService.makeDirectory(toDoFolderOfUser);

                        // to-check folder
                        String toCheckFolderOfUser = LAStringUtil.buildFolderPath(Constants.DASH + job.getProjectCode(), Constants.TO_CHECK, job.getName(), jobTeamUserDTO.getUserLogin());
                        fileSystemHandlingService.makeDirectory(toCheckFolderOfUser);

                        // done folder
                        String doneFolderOfUser = LAStringUtil.buildFolderPath(Constants.DASH + job.getProjectCode(), Constants.DONE, job.getName(), jobTeamUserDTO.getUserLogin());
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

                                    jobTeamUserTaskDTO.setOriginalFilePath(backLogItemPath);
                                    jobTeamUserTaskDTO.setOriginalFileName(remoteFileName);

                                    jobTeamUserTaskDTO.setStatus(FileStatusEnum.TODO);

                                    // setup file name
                                    String newFileName = job.getProjectCode() + Constants.UNDERSCORE + job.getName() + Constants.UNDERSCORE + sequenceDataDao.nextJobTeamUserTaskId() + Constants.UNDERSCORE + remoteFileName;
                                    jobTeamUserTaskDTO.setFileName(newFileName);

                                    jobTeamUserTaskDTO.setJobTeamUserId(jobTeamUserDTO.getId());
                                    jobTeamUserTaskDTOs.add(jobTeamUserTaskDTO);
                                    fileSystemHandlingService.copy(backLogItemPath + remoteFileName, toDoFolderOfUser, newFileName);

                                    totalFilesForJobTeam++;
                                    if (iFile >= files.size()) {

                                        break;
                                    }
                                }
                                jobTeamUserDTO.setTotalFiles(iActualTotalFiles - Constants.ONE);
                                jobTeamUserDTO.setJobTeamUserTasks(jobTeamUserTaskDTOs);
                            }

                        }
                    }
                }
                jobTeamDTO.setTotalFiles(totalFilesForJobTeam);
            }

            // re-update job
            jobService.save(job);

        }

        UpdatePlanResponseVM rs = new UpdatePlanResponseVM();
        rs.setJob(job);

        return rs;
    }
}
