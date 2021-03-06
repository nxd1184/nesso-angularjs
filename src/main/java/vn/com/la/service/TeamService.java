package vn.com.la.service;

import vn.com.la.service.dto.TeamDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.com.la.service.dto.param.SearchTeamParamDTO;
import vn.com.la.service.dto.param.UpdateTeamParamDTO;
import vn.com.la.web.rest.vm.response.EmptyResponseVM;

import java.util.List;

/**
 * Service Interface for managing Team.
 */
public interface TeamService {

    /**
     * Save a team.
     *
     * @param teamDTO the entity to save
     * @return the persisted entity
     */
    TeamDTO save(TeamDTO teamDTO);

    /**
     *  Get all the teams.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TeamDTO> findAll(Pageable pageable);

    List<TeamDTO> findAll();

    /**
     *  Get the "id" team.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    TeamDTO findOne(Long id);

    /**
     *  Delete the "id" team.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    Page<TeamDTO> search(Pageable pageable,
                         SearchTeamParamDTO criteria);

    EmptyResponseVM updateTeam(UpdateTeamParamDTO param) throws Exception;
}
