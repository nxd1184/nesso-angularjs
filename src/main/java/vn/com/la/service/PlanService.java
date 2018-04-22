package vn.com.la.service;

import vn.com.la.service.dto.param.*;
import vn.com.la.web.rest.vm.request.FinishJobRequestVM;
import vn.com.la.web.rest.vm.response.*;

public interface PlanService {

    JobPlanDetailResponseVM getPlanDetail(GetJobPlanDetailParamDTO params);

    UpdatePlanResponseVM updatePlan(UpdatePlanParamDTO params) throws Exception;

    GetAllPlanResponseVM getAllPlans(GetAllPlanParamDTO params);

    UserJobDetailResponseVM getUserJobDetail(GetUserJobDetailParamDTO params);

    EmptyResponseVM adjust(AdjustFilesParamDTO params) throws Exception;

    FinishJobResponseVM finish(FinishJobParamDTO params) throws Exception;
}
