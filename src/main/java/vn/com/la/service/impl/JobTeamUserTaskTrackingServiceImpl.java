package vn.com.la.service.impl;

import vn.com.la.service.JobTeamUserTaskTrackingService;
import vn.com.la.domain.JobTeamUserTaskTracking;
import vn.com.la.repository.JobTeamUserTaskTrackingRepository;
import vn.com.la.service.dto.JobTeamUserTaskTrackingDTO;
import vn.com.la.service.mapper.JobTeamUserTaskTrackingMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing JobTeamUserTaskTracking.
 */
@Service
@Transactional
public class JobTeamUserTaskTrackingServiceImpl implements JobTeamUserTaskTrackingService{

    private final Logger log = LoggerFactory.getLogger(JobTeamUserTaskTrackingServiceImpl.class);

    private final JobTeamUserTaskTrackingRepository jobTeamUserTaskTrackingRepository;

    private final JobTeamUserTaskTrackingMapper jobTeamUserTaskTrackingMapper;

    public JobTeamUserTaskTrackingServiceImpl(JobTeamUserTaskTrackingRepository jobTeamUserTaskTrackingRepository, JobTeamUserTaskTrackingMapper jobTeamUserTaskTrackingMapper) {
        this.jobTeamUserTaskTrackingRepository = jobTeamUserTaskTrackingRepository;
        this.jobTeamUserTaskTrackingMapper = jobTeamUserTaskTrackingMapper;
    }

    /**
     * Save a jobTeamUserTaskTracking.
     *
     * @param jobTeamUserTaskTrackingDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public JobTeamUserTaskTrackingDTO save(JobTeamUserTaskTrackingDTO jobTeamUserTaskTrackingDTO) {
        log.debug("Request to save JobTeamUserTaskTracking : {}", jobTeamUserTaskTrackingDTO);
        JobTeamUserTaskTracking jobTeamUserTaskTracking = jobTeamUserTaskTrackingMapper.toEntity(jobTeamUserTaskTrackingDTO);
        jobTeamUserTaskTracking = jobTeamUserTaskTrackingRepository.save(jobTeamUserTaskTracking);
        return jobTeamUserTaskTrackingMapper.toDto(jobTeamUserTaskTracking);
    }

    /**
     *  Get all the jobTeamUserTaskTrackings.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<JobTeamUserTaskTrackingDTO> findAll(Pageable pageable) {
        log.debug("Request to get all JobTeamUserTaskTrackings");
        return jobTeamUserTaskTrackingRepository.findAll(pageable)
            .map(jobTeamUserTaskTrackingMapper::toDto);
    }

    /**
     *  Get one jobTeamUserTaskTracking by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public JobTeamUserTaskTrackingDTO findOne(Long id) {
        log.debug("Request to get JobTeamUserTaskTracking : {}", id);
        JobTeamUserTaskTracking jobTeamUserTaskTracking = jobTeamUserTaskTrackingRepository.findOne(id);
        return jobTeamUserTaskTrackingMapper.toDto(jobTeamUserTaskTracking);
    }

    /**
     *  Delete the  jobTeamUserTaskTracking by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete JobTeamUserTaskTracking : {}", id);
        jobTeamUserTaskTrackingRepository.delete(id);
    }
}
