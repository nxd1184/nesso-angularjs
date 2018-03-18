package vn.com.la.service;

import org.springframework.security.core.Authentication;
import vn.com.la.web.rest.vm.response.NotifyResponseVM;

public interface NotificationService {
    public NotifyResponseVM getUserNotification(Authentication authentication);

}
