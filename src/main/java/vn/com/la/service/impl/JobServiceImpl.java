package vn.com.la.service.impl;

import vn.com.la.service.JobService;
import vn.com.la.domain.Job;
import vn.com.la.repository.JobRepository;
import vn.com.la.service.dto.JobDTO;
import vn.com.la.service.mapper.JobMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;


/**
 * Service Implementation for managing Job.
 */
@Service
@Transactional
public class JobServiceImpl implements JobService{

    private final Logger log = LoggerFactory.getLogger(JobServiceImpl.class);

    private final JobRepository jobRepository;

    private final JobMapper jobMapper;

    public JobServiceImpl(JobRepository jobRepository, JobMapper jobMapper) {
        this.jobRepository = jobRepository;
        this.jobMapper = jobMapper;
    }

    /**
     * Save a job.
     *
     * @param jobDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public JobDTO save(JobDTO jobDTO) {
        log.debug("Request to save Job : {}", jobDTO);
        Job job = jobMapper.toEntity(jobDTO);
        job = jobRepository.save(job);
        return jobMapper.toDto(job);
    }

    /**
     *  Get all the jobs.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<JobDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Jobs");
        return jobRepository.findAll(pageable)
            .map(jobMapper::toDto);
    }

    /**
     *  Get one job by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public JobDTO findOne(Long id) {
        log.debug("Request to get Job : {}", id);
        Job job = jobRepository.findOne(id);
        return jobMapper.toDto(job);
    }

    /**
     *  Delete the  job by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Job : {}", id);
        jobRepository.delete(id);
    }

    @Override
    public JobDTO findByNameAndProjectCode(String name, String projectCode) {
        log.debug("Request get Job by name : {}", name);
        Job job = jobRepository.findByNameAndProjectCode(name, projectCode);
        return jobMapper.toDto(job);
    }

    @Override
    public int updateJobToStart(Long id) {
        return jobRepository.updateJobToStarted(id);
    }

    @Override
    public Long getTotalReceiverByDateTime(ZonedDateTime fromDate, ZonedDateTime toDate) {
        Long total = jobRepository.sumReceiveByDateRange(fromDate, toDate);
        if(total == null) total = 0L;
        return total;
    }

    @Override
    public List<JobDTO> findByDeadlineBetween(ZonedDateTime fromDate, ZonedDateTime toDate) {
        List<Job> jobs = jobRepository.findByDeadlineBetweenOrderByDeadlineAsc(fromDate, toDate);
        return jobMapper.toDto(jobs);
    }

    @Override
    public int updateDeadLineAndCustomerRequirements(ZonedDateTime deadLine, String customerRequirement, Long jobId) {
        return jobRepository.updateDeadLineAndCustomerRequirements(deadLine, customerRequirement, jobId);
    }
}
