package vn.com.la.service;

import sun.invoke.empty.Empty;
import vn.com.la.service.dto.JobDTO;
import vn.com.la.service.dto.ProjectDTO;
import vn.com.la.service.dto.param.*;
import vn.com.la.web.rest.vm.response.*;

import java.util.List;

public interface PlanService {

    JobPlanDetailResponseVM getPlanDetail(GetJobPlanDetailParamDTO params);

    UpdatePlanResponseVM updatePlan(UpdatePlanParamDTO params) throws Exception;

    GetAllPlanResponseVM getAllPlans(GetAllPlanParamDTO params);

    UserJobDetailResponseVM getUserJobDetail(GetUserJobDetailParamDTO params);

    EmptyResponseVM adjust(AdjustFilesParamDTO params) throws Exception;
}
