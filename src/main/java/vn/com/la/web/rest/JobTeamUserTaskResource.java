package vn.com.la.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.apache.commons.lang3.StringUtils;
import vn.com.la.domain.enumeration.FileStatusEnum;
import vn.com.la.security.SecurityUtils;
import vn.com.la.service.JobTeamUserTaskService;
import vn.com.la.service.dto.param.DeliveryFilesParamDTO;
import vn.com.la.service.dto.param.SearchJobTeamUserTaskParamDTO;
import vn.com.la.web.rest.util.HeaderUtil;
import vn.com.la.web.rest.util.PaginationUtil;
import vn.com.la.service.dto.JobTeamUserTaskDTO;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.com.la.web.rest.vm.request.CheckInJobTeamUserTaskRequestVM;
import vn.com.la.web.rest.vm.request.DeliveryFilesRequestVM;
import vn.com.la.web.rest.vm.response.CheckInAllResponseVM;
import vn.com.la.web.rest.vm.response.EmptyResponseVM;
import vn.com.la.web.rest.vm.response.DeliveryFilesResponseVM;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing JobTeamUserTask.
 */
@RestController
@RequestMapping("/api")
public class JobTeamUserTaskResource {

    private final Logger log = LoggerFactory.getLogger(JobTeamUserTaskResource.class);

    private static final String ENTITY_NAME = "jobTeamUserTask";

    private final JobTeamUserTaskService jobTeamUserTaskService;

    public JobTeamUserTaskResource(JobTeamUserTaskService jobTeamUserTaskService) {
        this.jobTeamUserTaskService = jobTeamUserTaskService;
    }

    /**
     * POST  /job-team-user-tasks : Create a new jobTeamUserTask.
     *
     * @param jobTeamUserTaskDTO the jobTeamUserTaskDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new jobTeamUserTaskDTO, or with status 400 (Bad Request) if the jobTeamUserTask has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/job-team-user-tasks")
    @Timed
    public ResponseEntity<JobTeamUserTaskDTO> createJobTeamUserTask(@Valid @RequestBody JobTeamUserTaskDTO jobTeamUserTaskDTO) throws URISyntaxException {
        log.debug("REST request to save JobTeamUserTask : {}", jobTeamUserTaskDTO);
        if (jobTeamUserTaskDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new jobTeamUserTask cannot already have an ID")).body(null);
        }
        JobTeamUserTaskDTO result = jobTeamUserTaskService.save(jobTeamUserTaskDTO);
        return ResponseEntity.created(new URI("/api/job-team-user-tasks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /job-team-user-tasks : Updates an existing jobTeamUserTask.
     *
     * @param jobTeamUserTaskDTO the jobTeamUserTaskDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated jobTeamUserTaskDTO,
     * or with status 400 (Bad Request) if the jobTeamUserTaskDTO is not valid,
     * or with status 500 (Internal Server Error) if the jobTeamUserTaskDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/job-team-user-tasks")
    @Timed
    public ResponseEntity<JobTeamUserTaskDTO> updateJobTeamUserTask(@Valid @RequestBody JobTeamUserTaskDTO jobTeamUserTaskDTO) throws URISyntaxException {
        log.debug("REST request to update JobTeamUserTask : {}", jobTeamUserTaskDTO);
        if (jobTeamUserTaskDTO.getId() == null) {
            return createJobTeamUserTask(jobTeamUserTaskDTO);
        }
        JobTeamUserTaskDTO result = jobTeamUserTaskService.save(jobTeamUserTaskDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, jobTeamUserTaskDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /job-team-user-tasks : get all the jobTeamUserTasks.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of jobTeamUserTasks in body
     */
    @GetMapping("/job-team-user-tasks")
    @Timed
    public ResponseEntity<List<JobTeamUserTaskDTO>> getAllJobTeamUserTasks(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of JobTeamUserTasks");
        Page<JobTeamUserTaskDTO> page = jobTeamUserTaskService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/job-team-user-tasks");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /job-team-user-tasks/:id : get the "id" jobTeamUserTask.
     *
     * @param id the id of the jobTeamUserTaskDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the jobTeamUserTaskDTO, or with status 404 (Not Found)
     */
    @GetMapping("/job-team-user-tasks/{id}")
    @Timed
    public ResponseEntity<JobTeamUserTaskDTO> getJobTeamUserTask(@PathVariable Long id) {
        log.debug("REST request to get JobTeamUserTask : {}", id);
        JobTeamUserTaskDTO jobTeamUserTaskDTO = jobTeamUserTaskService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(jobTeamUserTaskDTO));
    }

    /**
     * DELETE  /job-team-user-tasks/:id : delete the "id" jobTeamUserTask.
     *
     * @param id the id of the jobTeamUserTaskDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/job-team-user-tasks/{id}")
    @Timed
    public ResponseEntity<Void> deleteJobTeamUserTask(@PathVariable Long id) {
        log.debug("REST request to delete JobTeamUserTask : {}", id);
        jobTeamUserTaskService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * GET  /job-team-user-tasks : get all the jobTeamUserTasks.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of jobTeamUserTasks in body
     */
    @GetMapping("/search-job-team-user-tasks")
    @Timed
    public ResponseEntity<List<JobTeamUserTaskDTO>> search(@ApiParam Pageable pageable,
                                                           @RequestParam(name = "statusList", required = false)  String statusList) {
        log.debug("REST request to search a page of JobTeamUserTasks");

        SearchJobTeamUserTaskParamDTO params = new SearchJobTeamUserTaskParamDTO();
        params.setAssignee(SecurityUtils.getCurrentUserLogin());
        if(StringUtils.isNotBlank(statusList)) {
            String[] separatedStatusList = statusList.split(",");
            List<FileStatusEnum> fileStatusEnumList = new ArrayList<>();
            for(String status: separatedStatusList) {
                FileStatusEnum fileStatusEnum = FileStatusEnum.valueOf(status);
                fileStatusEnumList.add(fileStatusEnum);
            }
            params.setStatusList(fileStatusEnumList);
        }

        Page<JobTeamUserTaskDTO> page = jobTeamUserTaskService.search(pageable, params);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/search-job-team-user-tasks");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @PutMapping("/check-in-job-team-user-task")
    @Timed
    public ResponseEntity<EmptyResponseVM> checkInJobTeamUserTask(@Valid @RequestBody CheckInJobTeamUserTaskRequestVM request) throws Exception {
        log.debug("REST request to checkin JobTeamUserTask : {}", request.getId());

        EmptyResponseVM rs = jobTeamUserTaskService.checkIn(request.getId());
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(rs));
    }

    @PutMapping("/check-in-all")
    @Timed
    public ResponseEntity<CheckInAllResponseVM> checkInAll() throws Exception {
        log.debug("REST request to check in all JobTeamUserTask");

        CheckInAllResponseVM rs = jobTeamUserTaskService.checkAll(SecurityUtils.getCurrentUserLogin());
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(rs));
    }

    @PutMapping("/delivery")
    @Timed
    public ResponseEntity<DeliveryFilesResponseVM> deliverFilesFromDoneToDeliveryFolder(@Valid @RequestBody DeliveryFilesRequestVM request) throws Exception {
        log.debug("REST request to deliver files from Done to Delivery");

        DeliveryFilesParamDTO params = new DeliveryFilesParamDTO();
        params.setFileNames(request.getFileNames());

        DeliveryFilesResponseVM rs = jobTeamUserTaskService.delivery(params);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(rs));
    }
}
