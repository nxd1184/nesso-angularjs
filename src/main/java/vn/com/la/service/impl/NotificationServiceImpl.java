package vn.com.la.service.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import vn.com.la.security.SecurityUtils;

import vn.com.la.service.NotificationService;
import vn.com.la.service.dto.NotificationDTO;
import org.joda.time.DateTime;

import vn.com.la.service.util.LADateTimeUtil;

import vn.com.la.web.rest.vm.response.NotifyResponseVM;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {
    private final Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);
    private final EntityManager entityManager;

    public NotificationServiceImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public NotifyResponseVM getUserNotification(Authentication authentication) {
        StringBuilder sqlBuilder = new StringBuilder();
        NotifyResponseVM rs = new NotifyResponseVM();
        Query query;

        DateTime now = DateTime.now();
        String username = SecurityUtils.getCurrentUserAuthentication(authentication);

        sqlBuilder.append("SELECT jtut.original_file_name, jtut.last_modified_date FROM job_team_user_task jtut ");
        sqlBuilder.append(" inner join job_team_user jtu on jtut.job_team_user_id = jtu.id");
        sqlBuilder.append(" inner join jhi_user ju on ju.id = jtu.user_id");
        if (SecurityUtils.isAuthenticationInRole(authentication, "ROLE_USER")) {
            sqlBuilder.append(" where (jtut.status = 'TODO' AND  TIMESTAMPDIFF(MINUTE, jtut.created_date, ?) >= 60 ) " +
                "OR (jtut.status = 'REWORK' AND  TIMESTAMPDIFF(MINUTE, jtut.last_rework_time, ?) >= 60 ) " +
                "AND ju.login = ?");
            query = entityManager.createNativeQuery(sqlBuilder.toString());
            query.setParameter(1, now.toString(LADateTimeUtil.DATETIME_FORMAT));
            query.setParameter(2, now.toString(LADateTimeUtil.DATETIME_FORMAT));
            query.setParameter(3, username);
        } else if (SecurityUtils.isAuthenticationInRole(authentication, "ROLE_QC")) {
            sqlBuilder.append(" where (jtut.status = 'TOCHECK' AND  TIMESTAMPDIFF(MINUTE, jtut.last_check_in_time , ?) >= 60 ) " +
                "AND ju.login = ?");
            query = entityManager.createNativeQuery(sqlBuilder.toString());
            query.setParameter(1, now.toString(LADateTimeUtil.DATETIME_FORMAT));
            query.setParameter(2, username);


        } else {
            return rs;
        }
        log.info(query.toString());
        List<Object[]> rows = query.getResultList();
        List<NotificationDTO> report = new ArrayList<>();
        for (Object[] row : rows) {
            NotificationDTO notificationDTO = new NotificationDTO();
            notificationDTO.setFileName(row[0].toString());
            notificationDTO.setTime(convertByteArrayToInstant((byte[])row[1]));
            report.add(notificationDTO);
        }
        rs.setReport(report);
        return rs;
    }

    private Instant convertByteArrayToInstant(byte[] bytes) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime ld = LocalDateTime.parse(new String(bytes, StandardCharsets.US_ASCII), fmt);
        Instant instant = ld.toInstant(ZoneOffset.UTC);
        return instant;
    }

}
