package vn.com.la.service;

import vn.com.la.service.dto.ProjectDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.com.la.web.rest.vm.response.SyncUpProjectResponseVM;

/**
 * Service Interface for managing Project.
 */
public interface ProjectService {

    /**
     * Save a project.
     *
     * @param projectDTO the entity to save
     * @return the persisted entity
     */
    ProjectDTO save(ProjectDTO projectDTO);

    /**
     *  Get all the projects.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ProjectDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" project.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ProjectDTO findOne(Long id);

    /**
     *  Delete the "id" project.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
    Page<ProjectDTO> findBySearchTerm(Pageable pageable, String searchTerm);

    SyncUpProjectResponseVM syncUp(String projectCode);

    ProjectDTO findByCode(String projectCode);
}
