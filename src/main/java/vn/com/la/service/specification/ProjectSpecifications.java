package vn.com.la.service.specification;

import org.springframework.data.jpa.domain.Specification;
import vn.com.la.domain.Project;
import vn.com.la.domain.Project_;

public class ProjectSpecifications {
    private ProjectSpecifications() {}

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
