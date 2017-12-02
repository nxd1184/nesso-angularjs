package vn.com.la.service.specification;

import org.springframework.data.jpa.domain.Specification;
import vn.com.la.domain.Team;
import vn.com.la.domain.Team_;
import vn.com.la.domain.User_;

public class TeamSpecifications {
    private TeamSpecifications() {}

    private static Specification<Team> nameOrMemberUserNameOrMemberEmail(String searchTerm) {
        return (root,query, cb) -> {
            String containsLikePattern = getContainsLikePattern(searchTerm);
            return cb.or(
                cb.like(cb.lower(root.get(Team_.name)), containsLikePattern)
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
