package vn.com.la.service.mapper;

import vn.com.la.domain.*;
import vn.com.la.service.dto.JobTeamUserTaskDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity JobTeamUserTask and its DTO JobTeamUserTaskDTO.
 */
@Mapper(componentModel = "spring", uses = {JobTeamUserMapper.class, UserMapper.class, })
public interface JobTeamUserTaskMapper extends EntityMapper <JobTeamUserTaskDTO, JobTeamUserTask> {

    @Mapping(source = "jobTeamUser.id", target = "jobTeamUserId")

    @Mapping(source = "assignee.id", target = "assigneeId")
    @Mapping(source = "assignee.login", target = "assigneeLogin")

    @Mapping(source = "qc.id", target = "qcId")
    @Mapping(source = "qc.login", target = "qcLogin")
    JobTeamUserTaskDTO toDto(JobTeamUserTask jobTeamUserTask); 

    @Mapping(source = "jobTeamUserId", target = "jobTeamUser")

    @Mapping(source = "assigneeId", target = "assignee")

    @Mapping(source = "qcId", target = "qc")
    JobTeamUserTask toEntity(JobTeamUserTaskDTO jobTeamUserTaskDTO); 
    default JobTeamUserTask fromId(Long id) {
        if (id == null) {
            return null;
        }
        JobTeamUserTask jobTeamUserTask = new JobTeamUserTask();
        jobTeamUserTask.setId(id);
        return jobTeamUserTask;
    }
}
