package vn.com.la.service;

import vn.com.la.service.dto.JobTeamUserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing JobTeamUser.
 */
public interface JobTeamUserService {

    /**
     * Save a jobTeamUser.
     *
     * @param jobTeamUserDTO the entity to save
     * @return the persisted entity
     */
    JobTeamUserDTO save(JobTeamUserDTO jobTeamUserDTO);

    /**
     *  Get all the jobTeamUsers.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<JobTeamUserDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" jobTeamUser.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    JobTeamUserDTO findOne(Long id);

    /**
     *  Delete the "id" jobTeamUser.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    int updateTotalFilesByJobTeamUserId(Long jobTeamUserId,Long newTotalFiles);
}
