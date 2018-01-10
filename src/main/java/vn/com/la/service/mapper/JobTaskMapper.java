package vn.com.la.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vn.com.la.domain.Job;
import vn.com.la.domain.JobTask;
import vn.com.la.domain.JobTeam;
import vn.com.la.service.dto.JobDTO;
import vn.com.la.service.dto.JobTaskDTO;
import vn.com.la.service.dto.JobTeamDTO;

/**
 * Mapper for the entity JobTask and its DTO JobTaskDTO.
 */
@Mapper(componentModel = "spring", uses = {JobMapper.class, TaskMapper.class})
public interface JobTaskMapper extends EntityMapper <JobTaskDTO, JobTask>{

    @Mapping(source = "task.id", target = "taskId")
    @Mapping(source = "job.id", target = "jobId")
    JobTaskDTO toDto(JobTask jobTask);

    @Mapping(source = "taskId", target = "task")
    @Mapping(source = "jobId", target = "job")
    JobTask toEntity(JobTaskDTO jobTaskDTO);
    default JobTask fromId(Long id) {
        if (id == null) {
            return null;
        }
        JobTask jobTask = new JobTask();
        jobTask.setId(id);
        return jobTask;
    }
}
