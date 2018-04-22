package vn.com.la.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.com.la.service.PlanService;
import vn.com.la.service.dto.PlanTypeEnumDTO;
import vn.com.la.service.dto.PlanViewEnumDTO;
import vn.com.la.service.dto.param.*;
import vn.com.la.service.util.LACommonUtil;
import vn.com.la.service.util.LADateTimeUtil;
import vn.com.la.web.rest.vm.request.AdjustFilesRequestVM;
import vn.com.la.web.rest.vm.request.CreateOrUpdatePlanRequestVM;
import vn.com.la.web.rest.vm.request.FinishJobRequestVM;
import vn.com.la.web.rest.vm.response.*;

import javax.validation.Valid;
import java.time.ZonedDateTime;
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
    public ResponseEntity<GetAllPlanResponseVM> getAllPlans(@ApiParam Pageable pageable, @RequestParam PlanViewEnumDTO view,
                                                            @RequestParam PlanTypeEnumDTO type,
                                                            @RequestParam(required = false, name = "projectCode") String projectCode,
                                                            @RequestParam(required = false, name = "taskCode") String taskCode,
                                                            @ApiParam String fromDate,
                                                            @ApiParam String toDate) {
        log.debug("REST request to get all plans");

        GetAllPlanParamDTO params = new GetAllPlanParamDTO();
        params.setType(type);
        params.setView(view);
        params.setProjectCode(projectCode);
        params.setTaskCode(taskCode);

        if(StringUtils.isNotBlank(fromDate)) {
            ZonedDateTime fromDateZDT = LADateTimeUtil.isoStringToZonedDateTime(fromDate);
            params.setFromDate(fromDateZDT);
        }

        if(StringUtils.isNotBlank(toDate)) {
            ZonedDateTime toDateZDT = LADateTimeUtil.isoStringToZonedDateTime(toDate);
            params.setToDate(toDateZDT);
        }

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
    public ResponseEntity<EmptyResponseVM> adjustFiles(@Valid @RequestBody AdjustFilesRequestVM request) throws Exception{

        AdjustFilesParamDTO params = new AdjustFilesParamDTO();
        params.setJobId(request.getJobId());
        params.setJobTeamUserId(request.getJobTeamUserId());
        params.setTotalFilesAdjustment(request.getTotalFilesAdjustment());
        params.setToUserId(request.getToUserId());
        EmptyResponseVM rs = planService.adjust(params);

        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(rs));
    }

    @PutMapping("/plan/finish")
    @Timed
    public ResponseEntity<FinishJobResponseVM> finish(@Valid @RequestBody FinishJobRequestVM request) throws Exception{

        FinishJobParamDTO params = new FinishJobParamDTO();
        params.setJobId(request.getJobId());

        FinishJobResponseVM rs = planService.finish(params);

        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(rs));
    }
}
