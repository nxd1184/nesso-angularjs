package vn.com.la.service.specification;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import vn.com.la.domain.Task;
import vn.com.la.domain.Task_;
import vn.com.la.domain.Project_;
import vn.com.la.service.dto.param.SearchTaskParamDTO;

import javax.persistence.criteria.Predicate;

public class TaskSpecifications {

    private TaskSpecifications() {}

    public static Specification<Task> taskCodeOrTaskNameContainsIgnoreCase(String searchTerm) {
        return (root,query, cb) -> {
            String containsLikePattern = getContainsLikePattern(searchTerm);
            return cb.or(
                cb.like(cb.lower(root.get(Task_.code)), containsLikePattern),
                cb.like(cb.lower(root.get(Task_.name)), containsLikePattern)
            );
        };
    }

    public static Specification<Task> search(SearchTaskParamDTO criteria) {
        return (root, query, cb) -> {
            Predicate p = null;
            if(StringUtils.isNotBlank(criteria.getSearchTerm())) {
                String containsLikePattern = getContainsLikePattern(criteria.getSearchTerm());
                p = cb.or(
                    cb.like(cb.lower(root.get(Task_.code)), containsLikePattern),
                    cb.like(cb.lower(root.get(Task_.name)), containsLikePattern)
                );
            }

            if(criteria.getProjectId() != null) {
                Predicate predicateByProjectId = cb.equal(root.get(Task_.project).get(Project_.id), criteria.getProjectId());
                if(p == null) {
                    p = predicateByProjectId;
                }else {
                    p = cb.and(
                        p,
                        predicateByProjectId
                    );
                }
            }

            return p;
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
