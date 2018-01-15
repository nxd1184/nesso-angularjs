package vn.com.la.service.impl;

import org.springframework.data.jpa.domain.Specification;
import vn.com.la.config.Constants;
import vn.com.la.domain.enumeration.FileStatusEnum;
import vn.com.la.service.FileSystemHandlingService;
import vn.com.la.service.JobService;
import vn.com.la.service.JobTeamUserTaskService;
import vn.com.la.domain.JobTeamUserTask;
import vn.com.la.repository.JobTeamUserTaskRepository;
import vn.com.la.service.dto.JobTeamUserTaskDTO;
import vn.com.la.service.dto.param.SearchJobTeamUserTaskParamDTO;
import vn.com.la.service.mapper.JobTeamUserTaskMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.la.service.specification.JobTeamUserTaskSpecifications;
import vn.com.la.service.util.LAStringUtil;
import vn.com.la.web.rest.errors.CustomParameterizedException;
import vn.com.la.web.rest.vm.response.EmptyResponseVM;


/**
 * Service Implementation for managing JobTeamUserTask.
 */
@Service
@Transactional
public class JobTeamUserTaskServiceImpl implements JobTeamUserTaskService{

    private final Logger log = LoggerFactory.getLogger(JobTeamUserTaskServiceImpl.class);

    private final JobTeamUserTaskRepository jobTeamUserTaskRepository;

    private final JobTeamUserTaskMapper jobTeamUserTaskMapper;

    private final JobService jobService;

    private final FileSystemHandlingService ftpService;

    public JobTeamUserTaskServiceImpl(JobTeamUserTaskRepository jobTeamUserTaskRepository, JobTeamUserTaskMapper jobTeamUserTaskMapper,
                                      JobService jobService, FileSystemHandlingService ftpService) {
        this.jobTeamUserTaskRepository = jobTeamUserTaskRepository;
        this.jobTeamUserTaskMapper = jobTeamUserTaskMapper;
        this.jobService = jobService;
        this.ftpService = ftpService;
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

    @Override
    public Page<JobTeamUserTaskDTO> search(Pageable pageable, SearchJobTeamUserTaskParamDTO params) {

        Specification<JobTeamUserTask> searchSpec = JobTeamUserTaskSpecifications.search(params);
        Page<JobTeamUserTaskDTO> page = jobTeamUserTaskRepository.findAll(searchSpec,pageable).map(jobTeamUserTaskMapper::toDto);

        return page;
    }

    @Override
    public EmptyResponseVM checkIn(Long id) throws Exception{
        JobTeamUserTaskDTO jobTeamUserTaskDTO = findOne(id);
        String path = LAStringUtil.buildFolderPath(Constants.DASH + jobTeamUserTaskDTO.getProjectCode(),
                                                            Constants.TO_CHECK,
                                                            jobTeamUserTaskDTO.getJobName(), jobTeamUserTaskDTO.getJobTeamUserLogin()) + jobTeamUserTaskDTO.getFileName();
        boolean fileExistOnToCheck = ftpService.checkFileExist(path);

        if(fileExistOnToCheck) {
            jobTeamUserTaskDTO.setStatus(FileStatusEnum.TOCHECK);
            save(jobTeamUserTaskDTO);
            jobService.updateJobToStart(jobTeamUserTaskDTO.getJobId());
            return new EmptyResponseVM();
        }

        throw new CustomParameterizedException("File " + jobTeamUserTaskDTO.getFileName() + " not found in to-check folder");


    }
}
