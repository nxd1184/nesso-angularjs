package vn.com.la.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.com.la.service.PlanService;
import vn.com.la.service.dto.PlanViewEnumDTO;
import vn.com.la.service.dto.param.GetAllPlanParamDTO;
import vn.com.la.service.dto.param.GetJobPlanDetailParamDTO;
import vn.com.la.service.dto.param.GetUserJobDetailParamDTO;
import vn.com.la.service.dto.param.UpdatePlanParamDTO;
import vn.com.la.service.util.LACommonUtil;
import vn.com.la.service.util.LADateTimeUtil;
import vn.com.la.web.rest.vm.request.AdjustFilesRequestVM;
import vn.com.la.web.rest.vm.request.CreateOrUpdatePlanRequestVM;
import vn.com.la.web.rest.vm.response.*;

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


    @GetMapping("/plans")
    @Timed
    public ResponseEntity<GetAllPlanResponseVM> getAllPlans(@ApiParam Pageable pageable, @RequestParam PlanViewEnumDTO view) {
        log.debug("REST request to get all plans");

        GetAllPlanParamDTO params = new GetAllPlanParamDTO();
        params.setView(view);

        GetAllPlanResponseVM rs = planService.getAllPlans(params);

        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(rs));
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
    public ResponseEntity<UpdatePlanResponseVM> update(@Valid @RequestBody CreateOrUpdatePlanRequestVM request) throws Exception{

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

    @GetMapping("/plan/user-detail/{userId}/{jobId}")
    @Timed
    public ResponseEntity<UserJobDetailResponseVM> getUserJobDetail(@NotEmpty @PathVariable Long userId,
                                                                    @NotEmpty @PathVariable Long jobId) {

        GetUserJobDetailParamDTO params = new GetUserJobDetailParamDTO();
        params.setJobTeamUserId(userId);
        params.setJobId(jobId);

        UserJobDetailResponseVM rs = planService.getUserJobDetail(params);

        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(rs));
    }

    @PutMapping("/plan/adjust")
    @Timed
    public ResponseEntity<EmptyResponseVM> adjustFiles(@Valid @RequestBody AdjustFilesRequestVM request) {

        return null;
    }
}
