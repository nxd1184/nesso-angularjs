package vn.com.la.service;

import org.springframework.security.core.Authentication;
import vn.com.la.web.rest.vm.response.NotifyResponseVM;

public interface NotifyService {
    public NotifyResponseVM getUserNotification(Authentication authentication);

}
