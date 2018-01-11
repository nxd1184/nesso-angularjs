package vn.com.la.service.specification;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import vn.com.la.domain.Task;
import vn.com.la.domain.Task_;
import vn.com.la.domain.Project_;
import vn.com.la.service.dto.param.SearchTaskParamDTO;

import javax.persistence.criteria.CriteriaBuilder;
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
                p = and(cb,p,predicateByProjectId);
            }

            if(criteria.getTaskId() != null) {
                Predicate predicateByTaskId = cb.equal(root.get(Task_.id), criteria.getTaskId());
                p = and(cb,p,predicateByTaskId);
            }

            return p;
        };
    }

    private static Predicate and(CriteriaBuilder cb, Predicate current, Predicate newPredicate) {
        if(current == null) {
            return newPredicate;
        }
        return cb.and(current, newPredicate);
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
