package vn.com.la.service;

import vn.com.la.service.dto.JobTeamDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing JobTeam.
 */
public interface JobTeamService {

    /**
     * Save a jobTeam.
     *
     * @param jobTeamDTO the entity to save
     * @return the persisted entity
     */
    JobTeamDTO save(JobTeamDTO jobTeamDTO);

    /**
     *  Get all the jobTeams.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<JobTeamDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" jobTeam.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    JobTeamDTO findOne(Long id);

    /**
     *  Delete the "id" jobTeam.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    int updateTotalFilesByJobTeamId(Long jobTeamId, Long totalFiles);
}
