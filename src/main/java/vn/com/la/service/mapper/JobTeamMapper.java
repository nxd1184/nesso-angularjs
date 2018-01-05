package vn.com.la.service.mapper;

import vn.com.la.domain.*;
import vn.com.la.service.dto.JobTeamDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity JobTeam and its DTO JobTeamDTO.
 */
@Mapper(componentModel = "spring", uses = {JobMapper.class, TeamMapper.class, ProjectMapper.class, JobTeamUserMapper.class})
public interface JobTeamMapper extends EntityMapper <JobTeamDTO, JobTeam> {

    @Mapping(source = "job.id", target = "jobId")
    @Mapping(source = "job.name", target = "jobName")

    @Mapping(source = "team.id", target = "teamId")
    @Mapping(source = "team.name", target = "teamName")

    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "project.name", target = "projectName")
    JobTeamDTO toDto(JobTeam jobTeam);

    @Mapping(source = "jobId", target = "job")

    @Mapping(source = "teamId", target = "team")

    @Mapping(source = "projectId", target = "project")
    JobTeam toEntity(JobTeamDTO jobTeamDTO);
    default JobTeam fromId(Long id) {
        if (id == null) {
            return null;
        }
        JobTeam jobTeam = new JobTeam();
        jobTeam.setId(id);
        return jobTeam;
    }
}
