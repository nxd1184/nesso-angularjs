package vn.com.la.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.la.domain.User;
import vn.com.la.security.SecurityUtils;
import vn.com.la.service.TimesheetService;
import vn.com.la.service.UserService;
import vn.com.la.service.dto.param.SubmitTimesheetParam;
import vn.com.la.web.rest.vm.request.SubmitTimesheetRequestVM;
import vn.com.la.web.rest.vm.response.EmptyResponseVM;

import java.util.Optional;

@RestController
@RequestMapping("/api/timesheet")
public class TimesheetAPI {

    private final Logger log = LoggerFactory.getLogger(TimesheetAPI.class);

    private static final String ENTITY_NAME = "timesheet";

    private final TimesheetService timesheetService;
    private final UserService userService;

    public TimesheetAPI(TimesheetService timesheetService, UserService userService) {
        this.timesheetService = timesheetService;
        this.userService = userService;
    }

    @PostMapping("/submit")
    @Timed
    public ResponseEntity<EmptyResponseVM> submitTimesheet() {

        EmptyResponseVM rs = new EmptyResponseVM();

        User user = userService.getUserWithAuthorities();

        SubmitTimesheetParam param = new SubmitTimesheetParam();
        param.setDate(DateTime.now().withTimeAtStartOfDay());
        param.setTime(DateTime.now());
        param.setUserId(user.getId());


        rs = timesheetService.submitTimesheet(param);

        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(rs));
    }
}
