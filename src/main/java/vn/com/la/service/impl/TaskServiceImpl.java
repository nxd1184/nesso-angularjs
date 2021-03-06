package vn.com.la.service.impl;

import org.springframework.data.jpa.domain.Specification;
import vn.com.la.service.TaskService;
import vn.com.la.domain.Task;
import vn.com.la.repository.TaskRepository;
import vn.com.la.service.dto.TaskDTO;
import vn.com.la.service.dto.param.SearchTaskParamDTO;
import vn.com.la.service.mapper.TaskMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.la.service.specification.TaskSpecifications;


/**
 * Service Implementation for managing Task.
 */
@Service
@Transactional(readOnly = true)
public class TaskServiceImpl implements TaskService{

    private final Logger log = LoggerFactory.getLogger(TaskServiceImpl.class);

    private final TaskRepository taskRepository;

    private final TaskMapper taskMapper;

    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    /**
     * Save a task.
     *
     * @param taskDTO the entity to save
     * @return the persisted entity
     */
    @Override
    @Transactional(readOnly = false)
    public TaskDTO save(TaskDTO taskDTO) {
        log.debug("Request to save Task : {}", taskDTO);
        Task task = taskMapper.toEntity(taskDTO);
        task = taskRepository.save(task);
        return taskMapper.toDto(task);
    }

    /**
     *  Get all the tasks.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    public Page<TaskDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Tasks");
        return taskRepository.findAll(pageable)
            .map(taskMapper::toDto);
    }

    @Override
    public Page<TaskDTO> search(Pageable pageable, SearchTaskParamDTO searchTaskCriteria) {
        Specification<Task> searchSpec = TaskSpecifications.search(searchTaskCriteria);
        Page<TaskDTO> page = taskRepository.findAll(searchSpec,pageable).map(TaskDTO::new);
        return page;
    }

    /**
     *  Get one task by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    public TaskDTO findOne(Long id) {
        log.debug("Request to get Task : {}", id);
        Task task = taskRepository.findOne(id);
        return taskMapper.toDto(task);
    }

    /**
     *  Delete the  task by id.
     *
     *  @param id the id of the entity
     */
    @Override
    @Transactional(readOnly = false)
    public void delete(Long id) {
        log.debug("Request to delete Task : {}", id);
        taskRepository.delete(id);
    }
}
