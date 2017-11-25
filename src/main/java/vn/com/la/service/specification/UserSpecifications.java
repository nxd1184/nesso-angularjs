package vn.com.la.service.specification;

import org.springframework.data.jpa.domain.Specification;

import vn.com.la.domain.User;
import vn.com.la.domain.User_;

public class UserSpecifications {

    private UserSpecifications() {}

    public static Specification<User> loginOrEmailContainsIgnoreCase(String searchTerm) {
        return (root,query, cb) -> {
            String containsLikePattern = getContainsLikePattern(searchTerm);
            return cb.or(
                cb.like(cb.lower(root.get(User_.login)), containsLikePattern),
                cb.like(cb.lower(root.get(User_.email)), containsLikePattern)
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
