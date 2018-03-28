package vn.com.la.service.impl;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;
import vn.com.la.domain.User;
import vn.com.la.repository.UserRepository;
import vn.com.la.service.TeamService;
import vn.com.la.domain.Team;
import vn.com.la.repository.TeamRepository;
import vn.com.la.service.dto.TeamDTO;
import vn.com.la.service.dto.UserDTO;
import vn.com.la.service.dto.param.SearchTeamParamDTO;
import vn.com.la.service.dto.param.TeamMemberParamDTO;
import vn.com.la.service.dto.param.UpdateTeamParamDTO;
import vn.com.la.service.mapper.TeamMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.la.service.mapper.UserMapper;
import vn.com.la.service.specification.TeamSpecifications;
import vn.com.la.service.util.LACollectionUtil;
import vn.com.la.web.rest.vm.response.EmptyResponseVM;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Service Implementation for managing Team.
 */
@Service
@Transactional(readOnly = true)
public class TeamServiceImpl implements TeamService{

    private final Logger log = LoggerFactory.getLogger(TeamServiceImpl.class);

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final TeamMapper teamMapper;

    public TeamServiceImpl(TeamRepository teamRepository, TeamMapper teamMapper, UserRepository userRepository, UserMapper userMapper) {
        this.teamRepository = teamRepository;
        this.teamMapper = teamMapper;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    /**
     * Save a team.
     *
     * @param teamDTO the entity to save
     * @return the persisted entity
     */
    @Override
    @Transactional(readOnly = false)
    public TeamDTO save(TeamDTO teamDTO) {
        log.debug("Request to save Team : {}", teamDTO);
        Team team = teamMapper.toEntity(teamDTO);

        team = teamRepository.save(team);

        return teamMapper.toDto(team);
    }

    /**
     *  Get all the teams.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TeamDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Teams");
        return teamRepository.findAll(pageable)
            .map(teamMapper::toDto);
    }

    /**
     *  Get one team by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public TeamDTO findOne(Long id) {
        log.debug("Request to get Team : {}", id);
        Team team = teamRepository.findOne(id);
        return teamMapper.toDto(team);
    }

    /**
     *  Delete the  team by id.
     *
     *  @param id the id of the entity
     */
    @Override
    @Transactional(readOnly = false)
    public void delete(Long id) {
        log.debug("Request to delete Team : {}", id);
        teamRepository.delete(id);
    }

    @Override
    public List<TeamDTO> findAll() {
        log.debug("Request to get all Teams");
        return teamRepository.findAll().stream()
            .map(teamMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public Page<TeamDTO> search(Pageable pageable, SearchTeamParamDTO criteria) {
        log.debug("Request to search Teams by search term");

        Specification<Team> searchSpec = TeamSpecifications.search(criteria);
        Page<TeamDTO> page = teamRepository.findAll(searchSpec, pageable).map(teamMapper::toDto);

        return page;
    }

    @Override
    @Transactional(readOnly = false)
    public EmptyResponseVM updateTeam(UpdateTeamParamDTO param) throws Exception{

        TeamDTO storedTeam = findOne(param.getTeamId());
        storedTeam.setName(param.getTeamName());
        storedTeam.setStatus(param.getStatus());

        Long oldLeaderId = storedTeam.getLeaderId();
        Long newLeaderId = param.getLeaderId();

        if(oldLeaderId != newLeaderId ) {
            storedTeam.setLeaderId(newLeaderId);
            if(oldLeaderId != null) {
                User oldLeader = userRepository.findOne(oldLeaderId);
                if(oldLeader != null) {
                    oldLeader.setTeam(null);
                    userRepository.save(oldLeader);
                }
            }
        }

        Map<Long, UserDTO> storedMembersMap = LACollectionUtil.map(storedTeam.getMembers(), new LACollectionUtil.MapBy<Long, UserDTO>() {
            @Override
            public Long by(UserDTO item) {
                return item.getId(); //
            }
        });

        Set<UserDTO> newMembers = new HashSet<>();
        List<User> storedUsers = new ArrayList<>();
        if(!CollectionUtils.isEmpty(param.getMembers())) {
            for(TeamMemberParamDTO memberParam: param.getMembers()) {
                User user = userRepository.findOne(memberParam.getId());
                if(user.getTeam() != null) {
                    if(user.getTeam().getId() != param.getTeamId()) {
                        throw new Exception("One User can not belongs to two teams");
                    }
                }
                if(user != null) {

                    user.setShift(memberParam.getShift());
                    user.setStartDate(memberParam.getStartDate());
                    user.setEndDate(memberParam.getEndDate());
                    user.setStatus(memberParam.getStatus());

                    storedUsers.add(user);

                    UserDTO storedUser = userMapper.userToUserDTO(user);
                    newMembers.add(storedUser);

                    if(storedMembersMap.containsKey(user.getId())) {
                        storedMembersMap.remove(user.getId());
                    }
                }
            }
        }

        List<User> outTeamUserList = new ArrayList<>();

        storedMembersMap.forEach((k, v) -> {
            User user = userRepository.findOne(v.getId());
            if(user != null) {
                user.setTeam(null);
            }
            outTeamUserList.add(user);
        });

        userRepository.save(outTeamUserList);

        storedTeam.setMembers(newMembers);

        save(storedTeam);

        EmptyResponseVM rs = new EmptyResponseVM();

        return rs;
    }
}
