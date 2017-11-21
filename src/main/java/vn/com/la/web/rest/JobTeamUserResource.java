package vn.com.la.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.com.la.service.JobTeamUserService;
import vn.com.la.web.rest.util.HeaderUtil;
import vn.com.la.web.rest.util.PaginationUtil;
import vn.com.la.service.dto.JobTeamUserDTO;
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
 * REST controller for managing JobTeamUser.
 */
@RestController
@RequestMapping("/api")
public class JobTeamUserResource {

    private final Logger log = LoggerFactory.getLogger(JobTeamUserResource.class);

    private static final String ENTITY_NAME = "jobTeamUser";

    private final JobTeamUserService jobTeamUserService;

    public JobTeamUserResource(JobTeamUserService jobTeamUserService) {
        this.jobTeamUserService = jobTeamUserService;
    }

    /**
     * POST  /job-team-users : Create a new jobTeamUser.
     *
     * @param jobTeamUserDTO the jobTeamUserDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new jobTeamUserDTO, or with status 400 (Bad Request) if the jobTeamUser has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/job-team-users")
    @Timed
    public ResponseEntity<JobTeamUserDTO> createJobTeamUser(@Valid @RequestBody JobTeamUserDTO jobTeamUserDTO) throws URISyntaxException {
        log.debug("REST request to save JobTeamUser : {}", jobTeamUserDTO);
        if (jobTeamUserDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new jobTeamUser cannot already have an ID")).body(null);
        }
        JobTeamUserDTO result = jobTeamUserService.save(jobTeamUserDTO);
        return ResponseEntity.created(new URI("/api/job-team-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /job-team-users : Updates an existing jobTeamUser.
     *
     * @param jobTeamUserDTO the jobTeamUserDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated jobTeamUserDTO,
     * or with status 400 (Bad Request) if the jobTeamUserDTO is not valid,
     * or with status 500 (Internal Server Error) if the jobTeamUserDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/job-team-users")
    @Timed
    public ResponseEntity<JobTeamUserDTO> updateJobTeamUser(@Valid @RequestBody JobTeamUserDTO jobTeamUserDTO) throws URISyntaxException {
        log.debug("REST request to update JobTeamUser : {}", jobTeamUserDTO);
        if (jobTeamUserDTO.getId() == null) {
            return createJobTeamUser(jobTeamUserDTO);
        }
        JobTeamUserDTO result = jobTeamUserService.save(jobTeamUserDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, jobTeamUserDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /job-team-users : get all the jobTeamUsers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of jobTeamUsers in body
     */
    @GetMapping("/job-team-users")
    @Timed
    public ResponseEntity<List<JobTeamUserDTO>> getAllJobTeamUsers(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of JobTeamUsers");
        Page<JobTeamUserDTO> page = jobTeamUserService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/job-team-users");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /job-team-users/:id : get the "id" jobTeamUser.
     *
     * @param id the id of the jobTeamUserDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the jobTeamUserDTO, or with status 404 (Not Found)
     */
    @GetMapping("/job-team-users/{id}")
    @Timed
    public ResponseEntity<JobTeamUserDTO> getJobTeamUser(@PathVariable Long id) {
        log.debug("REST request to get JobTeamUser : {}", id);
        JobTeamUserDTO jobTeamUserDTO = jobTeamUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(jobTeamUserDTO));
    }

    /**
     * DELETE  /job-team-users/:id : delete the "id" jobTeamUser.
     *
     * @param id the id of the jobTeamUserDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/job-team-users/{id}")
    @Timed
    public ResponseEntity<Void> deleteJobTeamUser(@PathVariable Long id) {
        log.debug("REST request to delete JobTeamUser : {}", id);
        jobTeamUserService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
