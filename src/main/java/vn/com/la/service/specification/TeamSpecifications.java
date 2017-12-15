package vn.com.la.service.specification;

import org.springframework.data.jpa.domain.Specification;
import vn.com.la.domain.Team;
import vn.com.la.domain.Team_;
import vn.com.la.domain.User_;

public class TeamSpecifications {
    private TeamSpecifications() {}

    public static Specification<Team> nameLikePattern(String name) {
        return (root,query, cb) -> {
            String containsLikePattern = getContainsLikePattern(name);
            return cb.like(cb.lower(root.get(Team_.name)), containsLikePattern);
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
