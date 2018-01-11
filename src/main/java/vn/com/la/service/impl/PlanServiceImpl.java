package vn.com.la.service.impl;

import org.apache.commons.net.ftp.FTPFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.la.config.Constants;
import vn.com.la.domain.JobTeam;
import vn.com.la.domain.JobTeamUser;
import vn.com.la.domain.JobTeamUserTask;
import vn.com.la.service.*;
import vn.com.la.service.dto.JobDTO;
import vn.com.la.service.dto.JobTeamDTO;
import vn.com.la.service.dto.JobTeamUserDTO;
import vn.com.la.service.dto.JobTeamUserTaskDTO;
import vn.com.la.service.dto.param.GetJobPlanDetailParamDTO;
import vn.com.la.service.dto.param.UpdatePlanParamDTO;
import vn.com.la.service.util.LAStringUtil;
import vn.com.la.web.rest.vm.response.JobPlanDetailResponseVM;
import vn.com.la.web.rest.vm.response.UpdatePlanResponseVM;

import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class PlanServiceImpl implements PlanService {

    private final JobService jobService;
    private final JobTeamService jobTeamService;
    private final FtpService ftpService;
    private final JobTeamUserTaskService jobTeamUserTaskService;

    public PlanServiceImpl(JobService jobService, JobTeamService jobTeamService, FtpService ftpService,
                           JobTeamUserTaskService jobTeamUserTaskService) {
        this.jobService = jobService;
        this.jobTeamService = jobTeamService;
        this.ftpService = ftpService;
        this.jobTeamUserTaskService = jobTeamUserTaskService;
    }

    @Override
    public JobPlanDetailResponseVM getPlanDetail(GetJobPlanDetailParamDTO params) {

        JobDTO job = jobService.findOne(params.getJobId());
        if(job != null) {
            if(job.getTotalFiles() == null) {
                job.setTotalFiles(Constants.ZERO.longValue());
            }

        }
        JobPlanDetailResponseVM rs = new JobPlanDetailResponseVM();
        rs.setJob(job);

        return rs;
    }

    @Override
    @Transactional(readOnly = false)
    public UpdatePlanResponseVM updatePlan(UpdatePlanParamDTO params) throws Exception{

        JobDTO job = jobService.findOne(params.getJobId());
        if(job != null) {
            job.setDeadline(params.getDeadline());
            job.setCustomerRequirements(params.getCustomerRequirements());
            job.setJobTasks(params.getTasks());
            job.setJobTeams(params.getTeams());
            job.setTotalFiles(params.getTotalFiles());
            if(job.getSequenceTask() == null) {
                job.setSequenceTask(params.getSequenceTask());
                job.setSequence(Constants.ONE);
            }

            jobService.save(job);

            // create backlog item folder inside todo, tocheck, done
            ftpService.makeDirectory(LAStringUtil.buildFolderPath( Constants.DASH + job.getProjectCode(), Constants.TO_DO, job.getName()));
            ftpService.makeDirectory(LAStringUtil.buildFolderPath( Constants.DASH + job.getProjectCode(), Constants.TO_CHECK, job.getName()));
            ftpService.makeDirectory(LAStringUtil.buildFolderPath( Constants.DASH + job.getProjectCode(), Constants.DONE, job.getName()));
            // move files from backlogs to to-do

            long totalFilesInBacklogItem = job.getTotalFiles();
            // list file from backlog item
            String backLogItemPath = LAStringUtil.buildFolderPath(Constants.DASH + job.getProjectCode(), Constants.BACK_LOGS, job.getName());
            List<FTPFile> files = ftpService.listFileFromPath(backLogItemPath);
            int iFile = 0;

            Set<JobTeamDTO> jobTeamDTOs = job.getJobTeams();
            for(JobTeamDTO jobTeamDTO: jobTeamDTOs) {
                if(jobTeamDTO.getJobTeamUsers() != null) {
                    // create user folder (as login name) into todo, tocheck, done of backlog item
                    for(JobTeamUserDTO jobTeamUserDTO: jobTeamDTO.getJobTeamUsers()) {
                        // To-do folder
                        String toDoFolderOfUser = LAStringUtil.buildFolderPath(Constants.DASH + job.getProjectCode(), Constants.TO_DO, job.getName(), jobTeamUserDTO.getUserLogin());
                        ftpService.makeDirectory(toDoFolderOfUser);

                        // to-check folder
                        String toCheckFolderOfUser = LAStringUtil.buildFolderPath(Constants.DASH + job.getProjectCode(), Constants.TO_CHECK, job.getName(), jobTeamUserDTO.getUserLogin());
                        ftpService.makeDirectory(toCheckFolderOfUser);

                        // done folder
                        String doneFolderOfUser = LAStringUtil.buildFolderPath(Constants.DASH + job.getProjectCode(), Constants.DONE, job.getName(), jobTeamUserDTO.getUserLogin());
                        ftpService.makeDirectory(doneFolderOfUser);

                        // move files from backlogs into todo folder
                        if(iFile >= files.size()) {
                            jobTeamDTO.setTotalFiles(new Long(Constants.ZERO));
                        }else {
                            if(jobTeamDTO.getTotalFiles() != null) {
                                long iActualTotalFiles = Constants.ZERO;
                                for(iActualTotalFiles = Constants.ONE; iActualTotalFiles <= jobTeamDTO.getTotalFiles(); iActualTotalFiles++) {

                                    FTPFile file = files.get(iFile++);
                                    String remoteFileName = file.getName();



                                    JobTeamUserTaskDTO jobTeamUserTaskDTO = new JobTeamUserTaskDTO();
                                    jobTeamUserTaskDTO.setFilePath(toDoFolderOfUser);
                                    jobTeamUserTaskDTO.setJobTeamUserId(jobTeamUserDTO.getId()); // assignee

                                    jobTeamUserTaskDTO.setOriginalFilePath(backLogItemPath);
                                    jobTeamUserTaskDTO.setOriginalFileName(remoteFileName);

                                    jobTeamUserTaskDTO = jobTeamUserTaskService.save(jobTeamUserTaskDTO);

                                    String newFileName = toDoFolderOfUser + Constants.DASH + job.getProjectCode() + Constants.UNDERSCORE + job.getName() + Constants.UNDERSCORE + jobTeamUserTaskDTO.getId() + Constants.UNDERSCORE + remoteFileName;

                                    ftpService.copy(backLogItemPath + remoteFileName, toDoFolderOfUser, newFileName);

                                    if(iFile >= files.size()) {
                                        break;
                                    }
                                }
                                jobTeamDTO.setTotalFiles(iActualTotalFiles);
                            }
                        }
                    }

                }
            }

        }

        UpdatePlanResponseVM rs = new UpdatePlanResponseVM();
        rs.setJob(job);

        return rs;
    }
}
