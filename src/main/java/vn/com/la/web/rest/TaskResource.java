package vn.com.la.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.apache.commons.lang3.StringUtils;
import vn.com.la.service.TaskService;
import vn.com.la.web.rest.util.HeaderUtil;
import vn.com.la.web.rest.util.PaginationUtil;
import vn.com.la.service.dto.TaskDTO;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.com.la.web.rest.vm.response.DatatableResponseVM;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Task.
 */
@RestController
@RequestMapping("/api")
public class TaskResource {

    private final Logger log = LoggerFactory.getLogger(TaskResource.class);

    private static final String ENTITY_NAME = "task";

    private final TaskService taskService;

    public TaskResource(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * POST  /tasks : Create a new task.
     *
     * @param taskDTO the taskDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new taskDTO, or with status 400 (Bad Request) if the task has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tasks")
    @Timed
    public ResponseEntity<TaskDTO> createTask(@Valid @RequestBody TaskDTO taskDTO) throws URISyntaxException {
        log.debug("REST request to save Task : {}", taskDTO);
        if (taskDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("task-management", "idexists", "A new task cannot already have an ID")).body(null);
        }
        TaskDTO result = taskService.save(taskDTO);
        return ResponseEntity.created(new URI("/api/tasks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tasks : Updates an existing task.
     *
     * @param taskDTO the taskDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated taskDTO,
     * or with status 400 (Bad Request) if the taskDTO is not valid,
     * or with status 500 (Internal Server Error) if the taskDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/tasks")
    @Timed
    public ResponseEntity<TaskDTO> updateTask(@Valid @RequestBody TaskDTO taskDTO) throws URISyntaxException {
        log.debug("REST request to update Task : {}", taskDTO);
        if (taskDTO.getId() == null) {
            return createTask(taskDTO);
        }
        TaskDTO result = taskService.save(taskDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, taskDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tasks : get all the tasks.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of tasks in body
     */
    @GetMapping("/tasks")
    @Timed
    public ResponseEntity<List<TaskDTO>> getAllTasks(@ApiParam Pageable pageable, @RequestParam(value = "search", required = false) String searchTerm) {
        log.debug("REST request to get a page of Tasks");
        Page<TaskDTO> page = taskService.findBySearchTerm(pageable, StringUtils.trimToEmpty(searchTerm));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tasks");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /tasks/search : search tasks.
     *
     * @param pageable the pagination information
     * @param searchTerm the searchTerm information
     * @return the ResponseEntity with status 200 (OK) and the list of tasks in body
     */
    @GetMapping("/tasks/search")
    @Timed
    public ResponseEntity<DatatableResponseVM> search(@ApiParam Pageable pageable, @RequestParam(value = "search", required = false) String searchTerm) {
        log.debug("REST request to search a page of Tasks");
        Page<TaskDTO> page = taskService.findBySearchTerm(pageable, StringUtils.trimToEmpty(searchTerm));

        DatatableResponseVM response = new DatatableResponseVM();
        response.setData(page.getContent());
        response.setRecordsFiltered(page.getTotalElements());
        response.setRecordsTotal(page.getTotalElements());

        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(response));
    }

    /**
     * GET  /tasks/:id : get the "id" task.
     *
     * @param id the id of the taskDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the taskDTO, or with status 404 (Not Found)
     */
    @GetMapping("/tasks/{id}")
    @Timed
    public ResponseEntity<TaskDTO> getTask(@PathVariable Long id) {
        log.debug("REST request to get Task : {}", id);
        TaskDTO taskDTO = taskService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(taskDTO));
    }

    /**
     * DELETE  /tasks/:id : delete the "id" task.
     *
     * @param id the id of the taskDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tasks/{id}")
    @Timed
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        log.debug("REST request to delete Task : {}", id);
        taskService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
