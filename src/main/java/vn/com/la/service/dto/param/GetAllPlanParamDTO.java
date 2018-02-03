package vn.com.la.service.dto.param;

import vn.com.la.service.dto.PlanViewEnumDTO;

public class GetAllPlanParamDTO {
    private PlanViewEnumDTO view;

    public PlanViewEnumDTO getView() {
        return view;
    }

    public void setView(PlanViewEnumDTO view) {
        this.view = view;
    }
}
