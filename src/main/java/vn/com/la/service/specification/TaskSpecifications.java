package vn.com.la.service.specification;

import org.springframework.data.jpa.domain.Specification;
import vn.com.la.domain.Task;
import vn.com.la.domain.Task_;

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

    private static String getContainsLikePattern(String searchTerm) {
        if (searchTerm == null || searchTerm.isEmpty()) {
            return "%%";
        }
        else {
            return "%" + searchTerm.toLowerCase() + "%";
        }
    }
}
