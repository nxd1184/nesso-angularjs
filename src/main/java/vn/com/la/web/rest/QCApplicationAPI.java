package vn.com.la.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.com.la.service.JobTeamUserTaskService;
import vn.com.la.service.dto.param.UpdateJobTeamUserTaskStatusParamDTO;
import vn.com.la.web.rest.vm.request.QCApplicationRequestVM;
import vn.com.la.web.rest.vm.response.EmptyResponseVM;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class QCApplicationAPI {

    private final Logger log = LoggerFactory.getLogger(QCApplicationAPI.class);

    private final JobTeamUserTaskService jobTeamUserTaskService;

    public QCApplicationAPI(JobTeamUserTaskService jobTeamUserTaskService) {
        this.jobTeamUserTaskService = jobTeamUserTaskService;
    }


    @PutMapping("/qc-app/rework")
    @Timed
    public ResponseEntity<EmptyResponseVM> rework(@Valid @RequestBody QCApplicationRequestVM request) throws Exception {
        log.debug("REST request to mark rework : {}");
        EmptyResponseVM rs = new EmptyResponseVM();

        if(StringUtils.isNotBlank(request.getFileName())) {
            UpdateJobTeamUserTaskStatusParamDTO params = new UpdateJobTeamUserTaskStatusParamDTO();
            params.setFileName(request.getFileName());
            rs = jobTeamUserTaskService.rework(params);
        }else {
            rs.setSuccess(false);
        }

        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(rs));
    }

    @PutMapping("/qc-app/edit")
    @Timed
    public ResponseEntity<EmptyResponseVM> qcEdit(@Valid @RequestBody QCApplicationRequestVM request) throws Exception {
        log.debug("REST request to mark QC Edit : {}");
        EmptyResponseVM rs = new EmptyResponseVM();
        if(StringUtils.isNotBlank(request.getFileName())) {
            UpdateJobTeamUserTaskStatusParamDTO params = new UpdateJobTeamUserTaskStatusParamDTO();
            params.setFileName(request.getFileName());
            rs = jobTeamUserTaskService.qcEdit(params);
        }else {
            rs.setSuccess(false);
        }
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(rs));
    }

    @PutMapping("/qc-app/done")
    @Timed
    public ResponseEntity<EmptyResponseVM> done(@Valid @RequestBody QCApplicationRequestVM request) throws Exception {
        log.debug("REST request to mark done : {}");
        EmptyResponseVM rs = new EmptyResponseVM();
        if(StringUtils.isNotBlank(request.getFileName())
            ) {
            UpdateJobTeamUserTaskStatusParamDTO params = new UpdateJobTeamUserTaskStatusParamDTO();
            params.setFileName(request.getFileName());
            rs = jobTeamUserTaskService.done(params);
        }else {
            rs.setSuccess(false);
        }
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(rs));
    }
}
