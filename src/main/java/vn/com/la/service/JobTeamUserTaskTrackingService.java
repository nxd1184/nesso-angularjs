package vn.com.la.service;

import vn.com.la.service.dto.JobTeamUserTaskTrackingDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing JobTeamUserTaskTracking.
 */
public interface JobTeamUserTaskTrackingService {

    /**
     * Save a jobTeamUserTaskTracking.
     *
     * @param jobTeamUserTaskTrackingDTO the entity to save
     * @return the persisted entity
     */
    JobTeamUserTaskTrackingDTO save(JobTeamUserTaskTrackingDTO jobTeamUserTaskTrackingDTO);

    /**
     *  Get all the jobTeamUserTaskTrackings.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<JobTeamUserTaskTrackingDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" jobTeamUserTaskTracking.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    JobTeamUserTaskTrackingDTO findOne(Long id);

    /**
     *  Delete the "id" jobTeamUserTaskTracking.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
