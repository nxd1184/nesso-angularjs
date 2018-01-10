package vn.com.la.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.la.config.Constants;
import vn.com.la.service.JobService;
import vn.com.la.service.JobTeamService;
import vn.com.la.service.PlanService;
import vn.com.la.service.dto.JobDTO;
import vn.com.la.service.dto.param.GetJobPlanDetailParamDTO;
import vn.com.la.service.dto.param.UpdatePlanParamDTO;
import vn.com.la.web.rest.vm.response.JobPlanDetailResponseVM;
import vn.com.la.web.rest.vm.response.UpdatePlanResponseVM;

@Service
@Transactional(readOnly = true)
public class PlanServiceImpl implements PlanService {

    private final JobService jobService;
    private final JobTeamService jobTeamService;

    public PlanServiceImpl(JobService jobService, JobTeamService jobTeamService) {
        this.jobService = jobService;
        this.jobTeamService = jobTeamService;
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
    public UpdatePlanResponseVM updatePlan(UpdatePlanParamDTO params) {

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
        }

        UpdatePlanResponseVM rs = new UpdatePlanResponseVM();
        rs.setJob(job);

        return rs;
    }
}
