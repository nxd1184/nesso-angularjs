package vn.com.la.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.la.security.SecurityUtils;
import vn.com.la.service.NotificationService;
import vn.com.la.web.rest.vm.response.DashboardResponseVM;
import vn.com.la.web.rest.vm.response.NotifyResponseVM;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class NotificationAPI {
    private final Logger log = LoggerFactory.getLogger(ReportAPI.class);

    private final NotificationService notificationService;

    public NotificationAPI(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/notification/get-notify")
    @Timed
    public ResponseEntity<NotifyResponseVM> getNotify() {
        NotifyResponseVM rs = notificationService.getUserNotification(SecurityUtils.getCurrentAuthentication());
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(rs));
    }

}
