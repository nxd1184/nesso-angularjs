package vn.com.la.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.com.la.service.PlanService;
import vn.com.la.service.dto.param.GetJobPlanDetailParamDTO;
import vn.com.la.service.dto.param.UpdatePlanParamDTO;
import vn.com.la.service.util.LACommonUtil;
import vn.com.la.service.util.LADateTimeUtil;
import vn.com.la.web.rest.vm.request.CreateOrUpdatePlanRequestVM;
import vn.com.la.web.rest.vm.response.JobPlanDetailResponseVM;
import vn.com.la.web.rest.vm.response.UpdatePlanResponseVM;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class PlanAPI {

    private final Logger log = LoggerFactory.getLogger(PlanAPI.class);

    private final PlanService planService;

    public PlanAPI(PlanService planService) {
        this.planService = planService;
    }

    @GetMapping("/plan/job-detail/{jobId}")
    @Timed
    public ResponseEntity<JobPlanDetailResponseVM> getJobPlanDetail(@NotEmpty @PathVariable Long jobId) {

        GetJobPlanDetailParamDTO params = new GetJobPlanDetailParamDTO();
        params.setJobId(jobId);

        JobPlanDetailResponseVM rs = planService.getPlanDetail(params);

        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(rs));
    }

    @PutMapping("/plan/update")
    @Timed
    public ResponseEntity<UpdatePlanResponseVM> update(@Valid @RequestBody CreateOrUpdatePlanRequestVM request) {

        String deadline = LACommonUtil.decode(request.getDeadline());

        UpdatePlanParamDTO params = new UpdatePlanParamDTO();
        params.setJobId(request.getJobId());
        params.setDeadline(LADateTimeUtil.isoStringToZonedDateTime(deadline));
        params.setTasks(request.getTasks());
        params.setTeams(request.getTeams());
        params.setSequenceTask(request.getSequenceTask());
        params.setCustomerRequirements(request.getCustomerRequirements());
        params.setTotalFiles(request.getTotalFiles());
        UpdatePlanResponseVM rs = planService.updatePlan(params);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(rs));
    }
}
