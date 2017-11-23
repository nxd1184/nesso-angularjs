package vn.com.la.service.impl;

import vn.com.la.service.TeamService;
import vn.com.la.domain.Team;
import vn.com.la.repository.TeamRepository;
import vn.com.la.service.dto.TeamDTO;
import vn.com.la.service.mapper.TeamMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


/**
 * Service Implementation for managing Team.
 */
@Service
@Transactional
public class TeamServiceImpl implements TeamService{

    private final Logger log = LoggerFactory.getLogger(TeamServiceImpl.class);

    private final TeamRepository teamRepository;

    private final TeamMapper teamMapper;

    public TeamServiceImpl(TeamRepository teamRepository, TeamMapper teamMapper) {
        this.teamRepository = teamRepository;
        this.teamMapper = teamMapper;
    }

    /**
     * Save a team.
     *
     * @param teamDTO the entity to save
     * @return the persisted entity
     */
    @Override
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
}
