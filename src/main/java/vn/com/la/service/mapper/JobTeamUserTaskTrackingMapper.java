package vn.com.la.service.mapper;

import vn.com.la.domain.*;
import vn.com.la.service.dto.JobTeamUserTaskTrackingDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity JobTeamUserTaskTracking and its DTO JobTeamUserTaskTrackingDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, JobTeamUserTaskMapper.class })
public interface JobTeamUserTaskTrackingMapper extends EntityMapper <JobTeamUserTaskTrackingDTO, JobTeamUserTaskTracking> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    @Mapping(source = "jobTeamUserTask.id", target = "jobTeamUserTaskId")
    JobTeamUserTaskTrackingDTO toDto(JobTeamUserTaskTracking jobTeamUserTaskTracking);

    @Mapping(source = "userId", target = "user")
    @Mapping(source = "jobTeamUserTaskId", target = "jobTeamUserTask")
    JobTeamUserTaskTracking toEntity(JobTeamUserTaskTrackingDTO jobTeamUserTaskTrackingDTO);
    default JobTeamUserTaskTracking fromId(Long id) {
        if (id == null) {
            return null;
        }
        JobTeamUserTaskTracking jobTeamUserTaskTracking = new JobTeamUserTaskTracking();
        jobTeamUserTaskTracking.setId(id);
        return jobTeamUserTaskTracking;
    }
}
