package vn.com.la.service;

import vn.com.la.service.dto.JobDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.ZonedDateTime;
import java.util.List;

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

    JobDTO findByNameAndProjectCode(String name, String projectCode);

    int updateJobToStart(Long id);

    Long getTotalReceiverByDateTime(ZonedDateTime fromDate, ZonedDateTime toDate);

    List<JobDTO> findByDeadlineBetween(ZonedDateTime fromDate, ZonedDateTime toDate);

    int updateDeadLineAndCustomerRequirements(ZonedDateTime deadLine, String customerRequirement, Long jobId);
}
