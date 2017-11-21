package vn.com.la.service.mapper;

import vn.com.la.domain.*;
import vn.com.la.service.dto.JobTeamUserDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity JobTeamUser and its DTO JobTeamUserDTO.
 */
@Mapper(componentModel = "spring", uses = {JobTeamMapper.class, UserMapper.class, })
public interface JobTeamUserMapper extends EntityMapper <JobTeamUserDTO, JobTeamUser> {

    @Mapping(source = "jobTeam.id", target = "jobTeamId")

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    JobTeamUserDTO toDto(JobTeamUser jobTeamUser); 

    @Mapping(source = "jobTeamId", target = "jobTeam")

    @Mapping(source = "userId", target = "user")
    JobTeamUser toEntity(JobTeamUserDTO jobTeamUserDTO); 
    default JobTeamUser fromId(Long id) {
        if (id == null) {
            return null;
        }
        JobTeamUser jobTeamUser = new JobTeamUser();
        jobTeamUser.setId(id);
        return jobTeamUser;
    }
}
