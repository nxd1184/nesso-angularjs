package vn.com.la.web.rest;

import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.la.service.JobService;
import vn.com.la.web.rest.util.HeaderUtil;
import vn.com.la.web.rest.vm.request.DashboardRequestVM;
import vn.com.la.web.rest.vm.response.DashboardResponseVM;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ReportAPI {

    private final Logger log = LoggerFactory.getLogger(ReportAPI.class);

    private final JobService jobService;

    public ReportAPI(JobService jobService) {
        this.jobService = jobService;
    }

    public ResponseEntity<DashboardResponseVM> getProductivityForDashboardThisWeek(@Valid @RequestBody DashboardRequestVM request) {

        log.debug("Request to get productivity for dashboard");

        DashboardResponseVM rs = new DashboardResponseVM();

        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(rs));
    }
}
