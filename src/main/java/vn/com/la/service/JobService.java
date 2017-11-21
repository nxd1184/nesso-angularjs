package vn.com.la.service;

import vn.com.la.service.dto.JobDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Job.
 */
public interface JobService {

    /**
     * Save a job.
     *
     * @param jobDTO the entity to save
     * @return the persisted entity
     */
    JobDTO save(JobDTO jobDTO);

    /**
     *  Get all the jobs.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<JobDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" job.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    JobDTO findOne(Long id);

    /**
     *  Delete the "id" job.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
