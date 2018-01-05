package vn.com.la.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.la.service.dto.JobDTO;
import vn.com.la.service.dto.param.GetJobPlanDetailParamDTO;
import vn.com.la.web.rest.vm.response.JobPlanDetailResponseVM;

@Service
@Transactional(readOnly = true)
public class PlanServiceImpl implements PlanService{

    private final JobService jobService;
    private final JobTeamService jobTeamService;

    public PlanServiceImpl(JobService jobService, JobTeamService jobTeamService) {
        this.jobService = jobService;
        this.jobTeamService = jobTeamService;
    }

    @Override
    public JobPlanDetailResponseVM getPlanDetail(GetJobPlanDetailParamDTO params) {

        JobDTO job = jobService.findOne(params.getJobId());

        JobPlanDetailResponseVM rs = new JobPlanDetailResponseVM();
        rs.setJob(job);

        return rs;
    }
}
