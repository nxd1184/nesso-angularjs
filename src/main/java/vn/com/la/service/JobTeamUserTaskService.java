package vn.com.la.service;

import vn.com.la.domain.JobTeamUserTask;
import vn.com.la.domain.enumeration.FileStatusEnum;
import vn.com.la.service.dto.JobTeamUserTaskDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.com.la.service.dto.param.DeliveryFilesParamDTO;
import vn.com.la.service.dto.param.SearchJobTeamUserTaskParamDTO;
import vn.com.la.service.dto.param.UpdateJobTeamUserTaskStatusParamDTO;
import vn.com.la.web.rest.vm.response.CheckInAllResponseVM;
import vn.com.la.web.rest.vm.response.EmptyResponseVM;
import vn.com.la.web.rest.vm.response.DeliveryFilesResponseVM;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * Service Interface for managing JobTeamUserTask.
 */
public interface JobTeamUserTaskService {

    /**
     * Save a jobTeamUserTask.
     *
     * @param jobTeamUserTaskDTO the entity to save
     * @return the persisted entity
     */
    JobTeamUserTaskDTO save(JobTeamUserTaskDTO jobTeamUserTaskDTO);
    List<JobTeamUserTaskDTO> save(List<JobTeamUserTaskDTO> jobTeamUserTaskDTOList);

    /**
     *  Get all the jobTeamUserTasks.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<JobTeamUserTaskDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" jobTeamUserTask.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    JobTeamUserTaskDTO findOne(Long id);
    JobTeamUserTask findEntityById(Long id);

    /**
     *  Delete the "id" jobTeamUserTask.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    Page<JobTeamUserTaskDTO> search(Pageable pageable, SearchJobTeamUserTaskParamDTO params);

    JobTeamUserTaskDTO findByFileName(String fileName);

    CheckInAllResponseVM checkAll(String assignee) throws Exception;
    EmptyResponseVM checkIn(Long id) throws Exception;
    EmptyResponseVM rework(UpdateJobTeamUserTaskStatusParamDTO params) throws Exception;
    EmptyResponseVM qcEdit(UpdateJobTeamUserTaskStatusParamDTO params) throws Exception;
    EmptyResponseVM done(UpdateJobTeamUserTaskStatusParamDTO params) throws Exception;

    Long countByStatusAndDateRange(List<FileStatusEnum> statusList, Instant fromDate, Instant toDate);
    DeliveryFilesResponseVM delivery(DeliveryFilesParamDTO params) throws Exception;

    List<JobTeamUserTask> findByJobTeamUserIdAndJobId(Long id, Long jobId);

    List<JobTeamUserTaskDTO> findJobToDoList(Long id, Long jobId);

    Long countJobToDoList(Long id, Long jobId);
    Long countNotDoneTask(Long jobId);

    Long sumNumberOfReworkByLastReworkTimeIsBetween(ZonedDateTime fromDate, ZonedDateTime toDate);

    int updateAdjustment(Long jobTeamUserId, String newFilePath, Long id);

    boolean deleteUnexpectedFile(String fileName) throws Exception;
}
