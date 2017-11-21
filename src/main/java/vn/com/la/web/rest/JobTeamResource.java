package vn.com.la.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.com.la.service.JobTeamService;
import vn.com.la.web.rest.util.HeaderUtil;
import vn.com.la.web.rest.util.PaginationUtil;
import vn.com.la.service.dto.JobTeamDTO;
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
 * REST controller for managing JobTeam.
 */
@RestController
@RequestMapping("/api")
public class JobTeamResource {

    private final Logger log = LoggerFactory.getLogger(JobTeamResource.class);

    private static final String ENTITY_NAME = "jobTeam";

    private final JobTeamService jobTeamService;

    public JobTeamResource(JobTeamService jobTeamService) {
        this.jobTeamService = jobTeamService;
    }

    /**
     * POST  /job-teams : Create a new jobTeam.
     *
     * @param jobTeamDTO the jobTeamDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new jobTeamDTO, or with status 400 (Bad Request) if the jobTeam has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/job-teams")
    @Timed
    public ResponseEntity<JobTeamDTO> createJobTeam(@Valid @RequestBody JobTeamDTO jobTeamDTO) throws URISyntaxException {
        log.debug("REST request to save JobTeam : {}", jobTeamDTO);
        if (jobTeamDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new jobTeam cannot already have an ID")).body(null);
        }
        JobTeamDTO result = jobTeamService.save(jobTeamDTO);
        return ResponseEntity.created(new URI("/api/job-teams/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /job-teams : Updates an existing jobTeam.
     *
     * @param jobTeamDTO the jobTeamDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated jobTeamDTO,
     * or with status 400 (Bad Request) if the jobTeamDTO is not valid,
     * or with status 500 (Internal Server Error) if the jobTeamDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/job-teams")
    @Timed
    public ResponseEntity<JobTeamDTO> updateJobTeam(@Valid @RequestBody JobTeamDTO jobTeamDTO) throws URISyntaxException {
        log.debug("REST request to update JobTeam : {}", jobTeamDTO);
        if (jobTeamDTO.getId() == null) {
            return createJobTeam(jobTeamDTO);
        }
        JobTeamDTO result = jobTeamService.save(jobTeamDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, jobTeamDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /job-teams : get all the jobTeams.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of jobTeams in body
     */
    @GetMapping("/job-teams")
    @Timed
    public ResponseEntity<List<JobTeamDTO>> getAllJobTeams(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of JobTeams");
        Page<JobTeamDTO> page = jobTeamService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/job-teams");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /job-teams/:id : get the "id" jobTeam.
     *
     * @param id the id of the jobTeamDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the jobTeamDTO, or with status 404 (Not Found)
     */
    @GetMapping("/job-teams/{id}")
    @Timed
    public ResponseEntity<JobTeamDTO> getJobTeam(@PathVariable Long id) {
        log.debug("REST request to get JobTeam : {}", id);
        JobTeamDTO jobTeamDTO = jobTeamService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(jobTeamDTO));
    }

    /**
     * DELETE  /job-teams/:id : delete the "id" jobTeam.
     *
     * @param id the id of the jobTeamDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/job-teams/{id}")
    @Timed
    public ResponseEntity<Void> deleteJobTeam(@PathVariable Long id) {
        log.debug("REST request to delete JobTeam : {}", id);
        jobTeamService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
