package vn.com.la.service;

import vn.com.la.service.dto.JobDTO;
import vn.com.la.service.dto.param.GetJobPlanDetailParamDTO;
import vn.com.la.service.dto.param.UpdatePlanParamDTO;
import vn.com.la.web.rest.vm.response.JobPlanDetailResponseVM;
import vn.com.la.web.rest.vm.response.UpdatePlanResponseVM;

public interface PlanService {

    JobPlanDetailResponseVM getPlanDetail(GetJobPlanDetailParamDTO params);

    UpdatePlanResponseVM updatePlan(UpdatePlanParamDTO params);
}
