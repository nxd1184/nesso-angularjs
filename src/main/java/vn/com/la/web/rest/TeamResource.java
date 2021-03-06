package vn.com.la.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import vn.com.la.service.TeamService;
import vn.com.la.service.dto.param.SearchTeamParamDTO;
import vn.com.la.service.dto.param.TeamMemberParamDTO;
import vn.com.la.service.dto.param.UpdateTeamParamDTO;
import vn.com.la.service.util.LACollectionUtil;
import vn.com.la.web.rest.util.HeaderUtil;
import vn.com.la.web.rest.util.PaginationUtil;
import vn.com.la.service.dto.TeamDTO;
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
import vn.com.la.web.rest.vm.request.TeamMemberRequestVM;
import vn.com.la.web.rest.vm.request.UpdateTeamRequestVM;
import vn.com.la.web.rest.vm.response.DatatableResponseVM;
import vn.com.la.web.rest.vm.response.EmptyResponseVM;
import vn.com.la.web.rest.vm.response.TeamListResponseVM;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Team.
 */
@RestController
@RequestMapping("/api")
public class TeamResource {

    private final Logger log = LoggerFactory.getLogger(TeamResource.class);

    private static final String ENTITY_NAME = "team";

    private final TeamService teamService;

    public TeamResource(TeamService teamService) {
        this.teamService = teamService;
    }

    /**
     * POST  /teams : Create a new team.
     *
     * @param teamDTO the teamDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new teamDTO, or with status 400 (Bad Request) if the team has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/teams")
    @Timed
    public ResponseEntity<TeamDTO> createTeam(@Valid @RequestBody TeamDTO teamDTO) throws URISyntaxException {
        log.debug("REST request to save Team : {}", teamDTO);
        if (teamDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new team cannot already have an ID")).body(null);
        }
        TeamDTO result = teamService.save(teamDTO);
        return ResponseEntity.created(new URI("/api/teams/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /teams/updateByProjectViewAndStatusType : Updates an existing team.
     *
     * @param request the teamDTO to updateByProjectViewAndStatusType
     * @return the ResponseEntity with status 200 (OK) and with body the updated request,
     * or with status 400 (Bad Request) if the request is not valid,
     * or with status 500 (Internal Server Error) if the request couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/teams/update")
    @Timed
    public ResponseEntity<EmptyResponseVM> updateTeam(@Valid @RequestBody UpdateTeamRequestVM request) throws Exception {

        UpdateTeamParamDTO param = new UpdateTeamParamDTO();
        param.setTeamId(request.getTeamId());
        param.setTeamName(request.getTeamName());
        param.setLeaderId(request.getLeaderId());
        param.setStatus(request.getStatus());

        List<TeamMemberParamDTO> memberParams = new ArrayList<>();
        if(!CollectionUtils.isEmpty(request.getMembers())) {
            for(TeamMemberRequestVM member: request.getMembers()) {
                memberParams.add(new TeamMemberParamDTO(member));
            }
        }
        param.setMembers(memberParams);

        EmptyResponseVM response = teamService.updateTeam(param);

        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(response));
    }

    /**
     * GET  /teams : get all the teams.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of teams in body
     */
    @GetMapping("/teams")
    @Timed
    public ResponseEntity<List<TeamDTO>> getAllTeams(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Teams");
        Page<TeamDTO> page = teamService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/teams");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/teams/search")
    @Timed
    public ResponseEntity<DatatableResponseVM> search(@ApiParam Pageable pageable, @ApiParam String searchTerm, @ApiParam Long teamId) {

        SearchTeamParamDTO searchTeamCriteria = new SearchTeamParamDTO();
        searchTeamCriteria.setSearchTerm(searchTerm);
        searchTeamCriteria.setTeamId(teamId);

        final Page<TeamDTO> page = teamService.search(pageable, searchTeamCriteria);

        DatatableResponseVM response = new DatatableResponseVM();
        response.setData(page.getContent());
        response.setRecordsFiltered(page.getTotalElements());
        response.setRecordsTotal(page.getTotalElements());

        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(response));
    }

    /**
     * GET  /teams/:id : get the "id" team.
     *
     * @param id the id of the teamDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the teamDTO, or with status 404 (Not Found)
     */
    @GetMapping("/teams/{id}")
    @Timed
    public ResponseEntity<TeamDTO> getTeam(@PathVariable Long id) {
        log.debug("REST request to get Team : {}", id);
        TeamDTO teamDTO = teamService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(teamDTO));
    }

    /**
     * DELETE  /teams/:id : delete the "id" team.
     *
     * @param id the id of the teamDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/teams/{id}")
    @Timed
    public ResponseEntity<Void> deleteTeam(@PathVariable Long id) {
        log.debug("REST request to delete Team : {}", id);
        teamService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * GET  /teams : get all the teams.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of teams in body
     */
    @GetMapping("/teams/all")
    @Timed
    public ResponseEntity<TeamListResponseVM> getAllTeams() {
        log.debug("REST request to get all Teams");
        List<TeamDTO> teams = teamService.findAll();
        TeamListResponseVM rs = new TeamListResponseVM();
        rs.setTeams(teams);

        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(rs));
    }
}
