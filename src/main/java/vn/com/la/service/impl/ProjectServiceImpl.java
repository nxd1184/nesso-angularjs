package vn.com.la.service.impl;

import org.apache.commons.net.ftp.FTP;
import org.springframework.data.jpa.domain.Specification;
import vn.com.la.domain.User;
import vn.com.la.service.FtpService;
import vn.com.la.service.JobService;
import vn.com.la.service.ProjectService;
import vn.com.la.domain.Project;
import vn.com.la.repository.ProjectRepository;
import vn.com.la.service.dto.JobDTO;
import vn.com.la.service.dto.ProjectDTO;
import vn.com.la.service.dto.UserDTO;
import vn.com.la.service.mapper.ProjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.la.service.util.LACollectionUtil;
import vn.com.la.web.rest.vm.response.EmptyResponseVM;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

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

    private final FtpService ftpService;

    private final JobService jobService;

    public ProjectServiceImpl(ProjectRepository projectRepository, ProjectMapper projectMapper,
                              FtpService ftpService, JobService jobService) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
        this.ftpService = ftpService;
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
    public ProjectDTO syncUp(String projectCode) {
        ProjectDTO projectDTO = findByCode(projectCode);
        try {
            List<String> backLogs = ftpService.backLogs(projectCode);
            if(projectDTO != null) {
                Set<JobDTO> jobDTOs = projectDTO.getJobs();

                java.util.Map<String, JobDTO> jobsMap = LACollectionUtil.map(jobDTOs, item -> {
                   return item.getName();
                });

                for(String backLog: backLogs) {
                    if(!jobsMap.containsKey(backLog)) {
                        JobDTO jobDTO = new JobDTO();
                        jobDTO.setName(backLog);
                        jobDTO.setProjectId(projectDTO.getId());
                        jobDTO = jobService.save(jobDTO);

                        projectDTO.addJob(jobDTO);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return projectDTO;
    }
}
