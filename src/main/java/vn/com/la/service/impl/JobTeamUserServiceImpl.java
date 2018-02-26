package vn.com.la.service.impl;

import vn.com.la.service.JobTeamUserService;
import vn.com.la.domain.JobTeamUser;
import vn.com.la.repository.JobTeamUserRepository;
import vn.com.la.service.dto.JobTeamUserDTO;
import vn.com.la.service.mapper.JobTeamUserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


/**
 * Service Implementation for managing JobTeamUser.
 */
@Service
@Transactional
public class JobTeamUserServiceImpl implements JobTeamUserService{

    private final Logger log = LoggerFactory.getLogger(JobTeamUserServiceImpl.class);

    private final JobTeamUserRepository jobTeamUserRepository;

    private final JobTeamUserMapper jobTeamUserMapper;

    public JobTeamUserServiceImpl(JobTeamUserRepository jobTeamUserRepository, JobTeamUserMapper jobTeamUserMapper) {
        this.jobTeamUserRepository = jobTeamUserRepository;
        this.jobTeamUserMapper = jobTeamUserMapper;
    }

    /**
     * Save a jobTeamUser.
     *
     * @param jobTeamUserDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public JobTeamUserDTO save(JobTeamUserDTO jobTeamUserDTO) {
        log.debug("Request to save JobTeamUser : {}", jobTeamUserDTO);
        JobTeamUser jobTeamUser = jobTeamUserMapper.toEntity(jobTeamUserDTO);
        jobTeamUser = jobTeamUserRepository.save(jobTeamUser);
        return jobTeamUserMapper.toDto(jobTeamUser);
    }

    /**
     *  Get all the jobTeamUsers.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<JobTeamUserDTO> findAll(Pageable pageable) {
        log.debug("Request to get all JobTeamUsers");
        return jobTeamUserRepository.findAll(pageable)
            .map(jobTeamUserMapper::toDto);
    }

    /**
     *  Get one jobTeamUser by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public JobTeamUserDTO findOne(Long id) {
        log.debug("Request to get JobTeamUser : {}", id);
        JobTeamUser jobTeamUser = jobTeamUserRepository.findOne(id);
        return jobTeamUserMapper.toDto(jobTeamUser);
    }

    /**
     *  Delete the  jobTeamUser by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete JobTeamUser : {}", id);
        jobTeamUserRepository.delete(id);
    }

    @Override
    public int updateTotalFilesByJobTeamUserId(Long jobTeamUserId, Long newTotalFiles) {
        return jobTeamUserRepository.updateTotalFilesByJobTeamUserId(jobTeamUserId, newTotalFiles);
    }

    @Override
    public List<JobTeamUserDTO> findByUserId(Long id) {
        return jobTeamUserRepository.findByUserId(id).stream().map(jobTeamUserMapper::toDto).collect(Collectors.toList());
    }
}
