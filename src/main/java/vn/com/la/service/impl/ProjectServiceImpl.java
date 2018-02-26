package vn.com.la.service.impl;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.data.jpa.domain.Specification;
import vn.com.la.config.Constants;
import vn.com.la.domain.enumeration.JobStatusEnum;
import vn.com.la.service.FileSystemHandlingService;
import vn.com.la.service.JobService;
import vn.com.la.service.ProjectService;
import vn.com.la.domain.Project;
import vn.com.la.repository.ProjectRepository;
import vn.com.la.service.dto.JobDTO;
import vn.com.la.service.dto.ProjectDTO;
import vn.com.la.service.dto.param.SearchProjectParamDTO;
import vn.com.la.service.mapper.ProjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.la.service.specification.ProjectSpecifications;
import vn.com.la.service.util.LACollectionUtil;
import vn.com.la.web.rest.errors.CustomParameterizedException;
import vn.com.la.web.rest.vm.response.SyncUpProjectResponseVM;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static vn.com.la.service.specification.ProjectSpecifications.codeContainsIgnoreCase;


/**
 * Service Implementation for managing Project.
 */
@Service
@Transactional(readOnly = true)
public class ProjectServiceImpl implements ProjectService{

    private final Logger log = LoggerFactory.getLogger(ProjectServiceImpl.class);

    private final ProjectRepository projectRepository;

    private final ProjectMapper projectMapper;

    private final FileSystemHandlingService fileSystemHandlingService;

    private final JobService jobService;

    public ProjectServiceImpl(ProjectRepository projectRepository, ProjectMapper projectMapper,
                              FileSystemHandlingService ftpService, JobService jobService) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
        this.fileSystemHandlingService = ftpService;
        this.jobService = jobService;
    }

    /**
     * Save a project.
     *
     * @param projectDTO the entity to save
     * @return the persisted entity
     */
    @Override
    @Transactional(readOnly = false)
    public ProjectDTO save(ProjectDTO projectDTO) {
        log.debug("Request to save Project : {}", projectDTO);
        Project project = projectMapper.toEntity(projectDTO);
        project = projectRepository.save(project);
        return projectMapper.toDto(project);
    }

    /**
     *  Get all the projects.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    public Page<ProjectDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Projects");
        return projectRepository.findAll(pageable)
            .map(projectMapper::toDto);
    }

    @Override
    public List<ProjectDTO> findAll() {
        return projectRepository.findAll().stream().map(projectMapper::toDto).collect(Collectors.toList());
    }

    /**
     *  Get one project by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    public ProjectDTO findOne(Long id) {
        log.debug("Request to get Project : {}", id);
        Project project = projectRepository.findOne(id);
        return projectMapper.toDto(project);
    }

    /**
     *  Delete the  project by id.
     *
     *  @param id the id of the entity
     */
    @Override
    @Transactional(readOnly = false)
    public void delete(Long id) {
        log.debug("Request to delete Project : {}", id);
        projectRepository.delete(id);
    }

    @Override
    public Page<ProjectDTO> findBySearchTerm(Pageable pageable, String searchTerm) {
        Specification<Project> searchSpec = codeContainsIgnoreCase(searchTerm);
        Page<ProjectDTO> page = projectRepository.findAll(searchSpec,pageable).map(projectMapper::toDto);
        return page;
    }

    @Override
    public ProjectDTO findByCode(String projectCode) {
        log.debug("Request to get Project : {}", projectCode);
        Project project = projectRepository.findByCode(projectCode);
        return projectMapper.toDto(project);
    }

    @Override
    @Transactional(readOnly = false)
    public SyncUpProjectResponseVM syncUp(String projectCode) {
        SyncUpProjectResponseVM rs = new SyncUpProjectResponseVM();
        List<JobDTO> syncJobs = new ArrayList<>();
        ProjectDTO projectDTO = findByCode(projectCode);
        try {
            List<String> backLogs = fileSystemHandlingService.backLogs(projectCode);
            if(projectDTO != null) {
                Set<JobDTO> jobDTOs = projectDTO.getJobs();

                java.util.Map<String, JobDTO> jobsMap = LACollectionUtil.map(jobDTOs, item -> {
                   return item.getName();
                });

                for(String backLog: backLogs) {
                    JobDTO jobDTO = null;
                    if(!jobsMap.containsKey(backLog)) {
                        jobDTO = new JobDTO();

                        jobDTO.setName(backLog);
                        jobDTO.setProjectId(projectDTO.getId());
                        String jobPath = Constants.DASH + projectCode + Constants.DASH + Constants.BACK_LOGS + Constants.DASH + backLog;
                        jobDTO.setTotalFiles(fileSystemHandlingService.countFilesFromPath(jobPath));
                        jobDTO.setStatus(JobStatusEnum.ACTIVE);
                        jobDTO.setSyncDate(ZonedDateTime.now());

                        projectDTO.addJob(jobDTO);

                    }else {
                        jobDTO = jobsMap.get(backLog);

                        jobDTO.setName(backLog);
                        jobDTO.setProjectId(projectDTO.getId());
                        String jobPath = Constants.DASH + projectCode + Constants.DASH + Constants.BACK_LOGS + Constants.DASH + backLog;
                        Long newTotalFiles = fileSystemHandlingService.countFilesFromPath(jobPath);
                        if(BooleanUtils.isTrue(jobDTO.getStarted())) {
                            if(newTotalFiles > jobDTO.getTotalFiles()) {
                                jobDTO.setTotalFiles(newTotalFiles);
                            }else {
                                if(BooleanUtils.isTrue(jobDTO.getStarted())) {
                                    throw new CustomParameterizedException("Total files is less than the current one");
                                }

                            }
                        }else {
                            jobDTO.setTotalFiles(newTotalFiles);
                        }


                        if(BooleanUtils.isNotTrue(jobDTO.getStarted())) {
                            jobDTO.setSyncDate(ZonedDateTime.now());
                        }

                        jobDTO.setStatus(JobStatusEnum.ACTIVE);

                    }

                    syncJobs.add(jobDTO);
                }

                save(projectDTO);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        rs.setJobs(syncJobs);
        return rs;
    }

    @Override
    public Page<ProjectDTO> search(SearchProjectParamDTO params, Pageable pageable) {
        Specification<Project> searchSpec = ProjectSpecifications.search(params);
        Page<ProjectDTO> page = projectRepository.findAll(searchSpec,pageable).map(projectMapper::toDto);
        return page;
    }
}
