package vn.com.la.service;

import vn.com.la.service.dto.JobDTO;
import vn.com.la.service.dto.ProjectDTO;
import vn.com.la.service.dto.param.GetJobPlanDetailParamDTO;
import vn.com.la.service.dto.param.UpdatePlanParamDTO;
import vn.com.la.web.rest.vm.response.JobPlanDetailResponseVM;
import vn.com.la.web.rest.vm.response.UpdatePlanResponseVM;

import java.util.List;

public interface PlanService {

    JobPlanDetailResponseVM getPlanDetail(GetJobPlanDetailParamDTO params);

    UpdatePlanResponseVM updatePlan(UpdatePlanParamDTO params) throws Exception;

    List<ProjectDTO> getAllPlans();
}
