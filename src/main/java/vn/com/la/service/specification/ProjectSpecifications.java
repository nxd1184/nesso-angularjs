package vn.com.la.service.specification;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import vn.com.la.domain.*;
import vn.com.la.service.dto.param.SearchProjectParamDTO;

import javax.persistence.criteria.Predicate;

public class ProjectSpecifications {
    private ProjectSpecifications() {}

    public static Specification<Project> search(SearchProjectParamDTO criteria) {
        return (root,query, cb) -> {
            Predicate p = null;

            if(criteria != null) {
                if(StringUtils.isNotBlank(criteria.getProjectCode())) {
                    String containsLikePattern = getContainsLikePattern(criteria.getProjectCode());
                    p = cb.like(root.get(Project_.code),containsLikePattern);
                }

                if(criteria.getStatus() != null) {
                    Predicate statusPredict = cb.equal(root.get(Project_.status), criteria.getStatus());
                    if(p == null) {
                        p = statusPredict;
                    }else {
                        p = cb.and(p, statusPredict);
                    }
                }

                if(StringUtils.isNotBlank(criteria.getTaskCode())) {
                    String containsLikePattern = getContainsLikePattern(criteria.getTaskCode());
                    Predicate predicate = cb.like(root.join(Project_.jobs).join(Job_.jobTasks).join(JobTask_.task).get(Task_.code), containsLikePattern);

                    if(p == null) {
                        p = predicate;
                    }else {
                        p = cb.and(
                            p,
                            predicate
                        );
                    }
                }
            }



            return p;
        };
    }

    public static Specification<Project> codeContainsIgnoreCase(String searchTerm) {
        return (root,query, cb) -> {
            String containsLikePattern = getContainsLikePattern(searchTerm);
            return cb.like(root.get(Project_.code),containsLikePattern);
        };
    }

    private static String getContainsLikePattern(String searchTerm) {
        if (searchTerm == null || searchTerm.isEmpty()) {
            return "%%";
        }
        else {
            return "%" + searchTerm.toLowerCase() + "%";
        }
    }
}
