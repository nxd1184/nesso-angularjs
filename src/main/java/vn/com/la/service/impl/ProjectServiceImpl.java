package vn.com.la.service.impl;

import org.springframework.data.jpa.domain.Specification;
import vn.com.la.domain.User;
import vn.com.la.service.ProjectService;
import vn.com.la.domain.Project;
import vn.com.la.repository.ProjectRepository;
import vn.com.la.service.dto.ProjectDTO;
import vn.com.la.service.dto.UserDTO;
import vn.com.la.service.mapper.ProjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static vn.com.la.service.specification.ProjectSpecifications.codeContainsIgnoreCase;


/**
 * Service Implementation for managing Project.
 */
@Service
@Transactional
public class ProjectServiceImpl implements ProjectService{

    private final Logger log = LoggerFactory.getLogger(ProjectServiceImpl.class);

    private final ProjectRepository projectRepository;

    private final ProjectMapper projectMapper;

    public ProjectServiceImpl(ProjectRepository projectRepository, ProjectMapper projectMapper) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
    }

    /**
     * Save a project.
     *
     * @param projectDTO the entity to save
     * @return the persisted entity
     */
    @Override
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
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
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
    public void delete(Long id) {
        log.debug("Request to delete Project : {}", id);
        projectRepository.delete(id);
    }

    @Transactional(readOnly = true)
    public Page<ProjectDTO> findBySearchTerm(Pageable pageable, String searchTerm) {
        Specification<Project> searchSpec = codeContainsIgnoreCase(searchTerm);
        Page<ProjectDTO> page = projectRepository.findAll(searchSpec,pageable).map(projectMapper::toDto);
        return page;
    }
}
