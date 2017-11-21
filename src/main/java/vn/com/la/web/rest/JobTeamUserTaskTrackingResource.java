package vn.com.la.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.com.la.service.JobTeamUserTaskTrackingService;
import vn.com.la.web.rest.util.HeaderUtil;
import vn.com.la.web.rest.util.PaginationUtil;
import vn.com.la.service.dto.JobTeamUserTaskTrackingDTO;
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

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing JobTeamUserTaskTracking.
 */
@RestController
@RequestMapping("/api")
public class JobTeamUserTaskTrackingResource {

    private final Logger log = LoggerFactory.getLogger(JobTeamUserTaskTrackingResource.class);

    private static final String ENTITY_NAME = "jobTeamUserTaskTracking";

    private final JobTeamUserTaskTrackingService jobTeamUserTaskTrackingService;

    public JobTeamUserTaskTrackingResource(JobTeamUserTaskTrackingService jobTeamUserTaskTrackingService) {
        this.jobTeamUserTaskTrackingService = jobTeamUserTaskTrackingService;
    }

    /**
     * POST  /job-team-user-task-trackings : Create a new jobTeamUserTaskTracking.
     *
     * @param jobTeamUserTaskTrackingDTO the jobTeamUserTaskTrackingDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new jobTeamUserTaskTrackingDTO, or with status 400 (Bad Request) if the jobTeamUserTaskTracking has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/job-team-user-task-trackings")
    @Timed
    public ResponseEntity<JobTeamUserTaskTrackingDTO> createJobTeamUserTaskTracking(@Valid @RequestBody JobTeamUserTaskTrackingDTO jobTeamUserTaskTrackingDTO) throws URISyntaxException {
        log.debug("REST request to save JobTeamUserTaskTracking : {}", jobTeamUserTaskTrackingDTO);
        if (jobTeamUserTaskTrackingDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new jobTeamUserTaskTracking cannot already have an ID")).body(null);
        }
        JobTeamUserTaskTrackingDTO result = jobTeamUserTaskTrackingService.save(jobTeamUserTaskTrackingDTO);
        return ResponseEntity.created(new URI("/api/job-team-user-task-trackings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /job-team-user-task-trackings : Updates an existing jobTeamUserTaskTracking.
     *
     * @param jobTeamUserTaskTrackingDTO the jobTeamUserTaskTrackingDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated jobTeamUserTaskTrackingDTO,
     * or with status 400 (Bad Request) if the jobTeamUserTaskTrackingDTO is not valid,
     * or with status 500 (Internal Server Error) if the jobTeamUserTaskTrackingDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/job-team-user-task-trackings")
    @Timed
    public ResponseEntity<JobTeamUserTaskTrackingDTO> updateJobTeamUserTaskTracking(@Valid @RequestBody JobTeamUserTaskTrackingDTO jobTeamUserTaskTrackingDTO) throws URISyntaxException {
        log.debug("REST request to update JobTeamUserTaskTracking : {}", jobTeamUserTaskTrackingDTO);
        if (jobTeamUserTaskTrackingDTO.getId() == null) {
            return createJobTeamUserTaskTracking(jobTeamUserTaskTrackingDTO);
        }
        JobTeamUserTaskTrackingDTO result = jobTeamUserTaskTrackingService.save(jobTeamUserTaskTrackingDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, jobTeamUserTaskTrackingDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /job-team-user-task-trackings : get all the jobTeamUserTaskTrackings.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of jobTeamUserTaskTrackings in body
     */
    @GetMapping("/job-team-user-task-trackings")
    @Timed
    public ResponseEntity<List<JobTeamUserTaskTrackingDTO>> getAllJobTeamUserTaskTrackings(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of JobTeamUserTaskTrackings");
        Page<JobTeamUserTaskTrackingDTO> page = jobTeamUserTaskTrackingService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/job-team-user-task-trackings");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /job-team-user-task-trackings/:id : get the "id" jobTeamUserTaskTracking.
     *
     * @param id the id of the jobTeamUserTaskTrackingDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the jobTeamUserTaskTrackingDTO, or with status 404 (Not Found)
     */
    @GetMapping("/job-team-user-task-trackings/{id}")
    @Timed
    public ResponseEntity<JobTeamUserTaskTrackingDTO> getJobTeamUserTaskTracking(@PathVariable Long id) {
        log.debug("REST request to get JobTeamUserTaskTracking : {}", id);
        JobTeamUserTaskTrackingDTO jobTeamUserTaskTrackingDTO = jobTeamUserTaskTrackingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(jobTeamUserTaskTrackingDTO));
    }

    /**
     * DELETE  /job-team-user-task-trackings/:id : delete the "id" jobTeamUserTaskTracking.
     *
     * @param id the id of the jobTeamUserTaskTrackingDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/job-team-user-task-trackings/{id}")
    @Timed
    public ResponseEntity<Void> deleteJobTeamUserTaskTracking(@PathVariable Long id) {
        log.debug("REST request to delete JobTeamUserTaskTracking : {}", id);
        jobTeamUserTaskTrackingService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
