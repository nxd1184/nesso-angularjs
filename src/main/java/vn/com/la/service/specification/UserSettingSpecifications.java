package vn.com.la.service.specification;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import vn.com.la.domain.UserSetting;
import vn.com.la.domain.UserSetting_;
import vn.com.la.service.dto.param.SearchUserSettingParamDTO;
import vn.com.la.service.util.LADateTimeUtil;

import javax.persistence.criteria.Predicate;
import java.time.Instant;
import java.time.ZonedDateTime;

public class UserSettingSpecifications {
    private UserSettingSpecifications() {}

    public static Specification<UserSetting> search(SearchUserSettingParamDTO params) {
        return (root,query, cb) -> {
            Predicate p = null;

            if(StringUtils.isNotBlank(params.getName())) {
                String containsLikePattern = getContainsLikePattern(params.getName());
                p = cb.like(cb.lower(root.get(UserSetting_.name)), containsLikePattern);
            }

            if(params.getDate() != null) {
                Instant startTimeOfDate = LADateTimeUtil.toStartOfDate(params.getDate()).toInstant();
                Instant endTimeOfDate = LADateTimeUtil.toMidnightOfDate(params.getDate()).toInstant();

                Predicate predicateByDate = cb.between(root.get(UserSetting_.createdDate), startTimeOfDate, endTimeOfDate);

                if(p == null) {
                    p = predicateByDate;
                }else {
                    p = cb.and(p, predicateByDate);
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
