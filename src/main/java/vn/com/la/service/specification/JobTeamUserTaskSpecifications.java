package vn.com.la.service.specification;

import org.springframework.data.jpa.domain.Specification;
import vn.com.la.domain.*;
import vn.com.la.domain.enumeration.FileStatusEnum;
import vn.com.la.service.dto.param.SearchJobTeamUserTaskParamDTO;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;


public class JobTeamUserTaskSpecifications {

    private JobTeamUserTaskSpecifications() {

    }

    public static Specification<JobTeamUserTask> search(SearchJobTeamUserTaskParamDTO params) {
        return (root,query, cb) -> {
            Predicate p = null;
            if(params.getAssignee() != null) {
                p = cb.equal(root.get(JobTeamUserTask_.jobTeamUser).get(JobTeamUser_.user).get(User_.login), params.getAssignee());
            }

            if(params.getStatusList() != null) {
                CriteriaBuilder.In<FileStatusEnum> inFileStatusEnum = cb.in(root.get(JobTeamUserTask_.status));
                for(FileStatusEnum status: params.getStatusList()) {
                    inFileStatusEnum.value(status);
                }
                if(p == null) {
                    p = inFileStatusEnum;
                }else {
                    p = cb.and(p, inFileStatusEnum);
                }
            }

            return p;
        };
    }
}
