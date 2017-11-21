package vn.com.la.service.impl;

import vn.com.la.service.JobTeamService;
import vn.com.la.domain.JobTeam;
import vn.com.la.repository.JobTeamRepository;
import vn.com.la.service.dto.JobTeamDTO;
import vn.com.la.service.mapper.JobTeamMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing JobTeam.
 */
@Service
@Transactional
public class JobTeamServiceImpl implements JobTeamService{

    private final Logger log = LoggerFactory.getLogger(JobTeamServiceImpl.class);

    private final JobTeamRepository jobTeamRepository;

    private final JobTeamMapper jobTeamMapper;

    public JobTeamServiceImpl(JobTeamRepository jobTeamRepository, JobTeamMapper jobTeamMapper) {
        this.jobTeamRepository = jobTeamRepository;
        this.jobTeamMapper = jobTeamMapper;
    }

    /**
     * Save a jobTeam.
     *
     * @param jobTeamDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public JobTeamDTO save(JobTeamDTO jobTeamDTO) {
        log.debug("Request to save JobTeam : {}", jobTeamDTO);
        JobTeam jobTeam = jobTeamMapper.toEntity(jobTeamDTO);
        jobTeam = jobTeamRepository.save(jobTeam);
        return jobTeamMapper.toDto(jobTeam);
    }

    /**
     *  Get all the jobTeams.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<JobTeamDTO> findAll(Pageable pageable) {
        log.debug("Request to get all JobTeams");
        return jobTeamRepository.findAll(pageable)
            .map(jobTeamMapper::toDto);
    }

    /**
     *  Get one jobTeam by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public JobTeamDTO findOne(Long id) {
        log.debug("Request to get JobTeam : {}", id);
        JobTeam jobTeam = jobTeamRepository.findOne(id);
        return jobTeamMapper.toDto(jobTeam);
    }

    /**
     *  Delete the  jobTeam by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete JobTeam : {}", id);
        jobTeamRepository.delete(id);
    }
}
