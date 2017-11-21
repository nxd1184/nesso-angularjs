package vn.com.la.service.impl;

import vn.com.la.service.JobTeamUserTaskService;
import vn.com.la.domain.JobTeamUserTask;
import vn.com.la.repository.JobTeamUserTaskRepository;
import vn.com.la.service.dto.JobTeamUserTaskDTO;
import vn.com.la.service.mapper.JobTeamUserTaskMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing JobTeamUserTask.
 */
@Service
@Transactional
public class JobTeamUserTaskServiceImpl implements JobTeamUserTaskService{

    private final Logger log = LoggerFactory.getLogger(JobTeamUserTaskServiceImpl.class);

    private final JobTeamUserTaskRepository jobTeamUserTaskRepository;

    private final JobTeamUserTaskMapper jobTeamUserTaskMapper;

    public JobTeamUserTaskServiceImpl(JobTeamUserTaskRepository jobTeamUserTaskRepository, JobTeamUserTaskMapper jobTeamUserTaskMapper) {
        this.jobTeamUserTaskRepository = jobTeamUserTaskRepository;
        this.jobTeamUserTaskMapper = jobTeamUserTaskMapper;
    }

    /**
     * Save a jobTeamUserTask.
     *
     * @param jobTeamUserTaskDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public JobTeamUserTaskDTO save(JobTeamUserTaskDTO jobTeamUserTaskDTO) {
        log.debug("Request to save JobTeamUserTask : {}", jobTeamUserTaskDTO);
        JobTeamUserTask jobTeamUserTask = jobTeamUserTaskMapper.toEntity(jobTeamUserTaskDTO);
        jobTeamUserTask = jobTeamUserTaskRepository.save(jobTeamUserTask);
        return jobTeamUserTaskMapper.toDto(jobTeamUserTask);
    }

    /**
     *  Get all the jobTeamUserTasks.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<JobTeamUserTaskDTO> findAll(Pageable pageable) {
        log.debug("Request to get all JobTeamUserTasks");
        return jobTeamUserTaskRepository.findAll(pageable)
            .map(jobTeamUserTaskMapper::toDto);
    }

    /**
     *  Get one jobTeamUserTask by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public JobTeamUserTaskDTO findOne(Long id) {
        log.debug("Request to get JobTeamUserTask : {}", id);
        JobTeamUserTask jobTeamUserTask = jobTeamUserTaskRepository.findOne(id);
        return jobTeamUserTaskMapper.toDto(jobTeamUserTask);
    }

    /**
     *  Delete the  jobTeamUserTask by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete JobTeamUserTask : {}", id);
        jobTeamUserTaskRepository.delete(id);
    }
}
