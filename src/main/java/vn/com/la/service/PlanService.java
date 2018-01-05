package vn.com.la.service;

import vn.com.la.service.dto.param.GetJobPlanDetailParamDTO;
import vn.com.la.web.rest.vm.response.JobPlanDetailResponseVM;

public interface PlanService {

    JobPlanDetailResponseVM getPlanDetail(GetJobPlanDetailParamDTO params);
}
