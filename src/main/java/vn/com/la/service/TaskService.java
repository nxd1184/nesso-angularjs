package vn.com.la.service;

import vn.com.la.service.dto.TaskDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.com.la.service.dto.param.SearchTaskParamDTO;

/**
 * Service Interface for managing Task.
 */
public interface TaskService {

    /**
     * Save a task.
     *
     * @param taskDTO the entity to save
     * @return the persisted entity
     */
    TaskDTO save(TaskDTO taskDTO);

    /**
     *  Get all the tasks.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TaskDTO> findAll(Pageable pageable);

    /**
     *  Get all the tasks by search term
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TaskDTO> search(Pageable pageable, SearchTaskParamDTO searchTaskCriteria);

    /**
     *  Get the "id" task.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    TaskDTO findOne(Long id);

    /**
     *  Delete the "id" task.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
