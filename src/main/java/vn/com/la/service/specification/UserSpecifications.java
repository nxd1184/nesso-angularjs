package vn.com.la.service.specification;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import org.springframework.util.CollectionUtils;
import vn.com.la.domain.Authority;
import vn.com.la.domain.Authority_;
import vn.com.la.domain.User;
import vn.com.la.domain.User_;
import vn.com.la.service.dto.param.SearchUserParamDTO;

import javax.persistence.criteria.Predicate;

public class UserSpecifications {

    private UserSpecifications() {}

    public static Specification<User> search(SearchUserParamDTO params) {
        return (root,query, cb) -> {
            Predicate p = null;
            if(StringUtils.isNotBlank(params.getSearchTerms())) {
                String containsLikePattern = getContainsLikePattern(params.getSearchTerms());
                p = cb.or(
                    cb.like(cb.lower(root.get(User_.login)), containsLikePattern),
                    cb.like(cb.lower(root.get(User_.email)), containsLikePattern)
                );
            }

            if(!CollectionUtils.isEmpty(params.getAuthorities())) {
                Predicate predicate = root.join(User_.authorities).get(Authority_.name).in(params.getAuthorities());
                if(p == null) {
                    p = predicate;
                }else {
                    p = cb.and(
                        p,
                        predicate
                    );
                }
            }

            return p;
        };
    }

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
