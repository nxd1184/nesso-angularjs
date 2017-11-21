package vn.com.la.service.mapper;

import vn.com.la.domain.*;
import vn.com.la.service.dto.TeamDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Team and its DTO TeamDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, })
public interface TeamMapper extends EntityMapper <TeamDTO, Team> {

    @Mapping(source = "leader.id", target = "leaderId")
    @Mapping(source = "leader.login", target = "leaderLogin")
    TeamDTO toDto(Team team); 

    @Mapping(source = "leaderId", target = "leader")
    Team toEntity(TeamDTO teamDTO); 
    default Team fromId(Long id) {
        if (id == null) {
            return null;
        }
        Team team = new Team();
        team.setId(id);
        return team;
    }
}
