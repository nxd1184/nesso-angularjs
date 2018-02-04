package vn.com.la.service.specification;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import vn.com.la.domain.Team;
import vn.com.la.domain.Team_;
import vn.com.la.domain.User_;
import vn.com.la.service.dto.param.SearchTeamParamDTO;

import javax.persistence.criteria.Predicate;

public class TeamSpecifications {
    private TeamSpecifications() {}

    public static Specification<Team> search(SearchTeamParamDTO criteria) {
        return (root,query, cb) -> {
            Predicate p = null;
            if(StringUtils.isNotBlank(criteria.getSearchTerm())) {
                String name = getContainsLikePattern(criteria.getSearchTerm());
                p = cb.like(cb.lower(root.get(Team_.name)), name);
            }

            if(criteria.getTeamId() != null) {
                Predicate predicateForTeamId = cb.equal(root.get(Team_.id), criteria.getTeamId());
                if(p != null) {
                    p = cb.and(p, predicateForTeamId);
                }else {
                    p = predicateForTeamId;
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
