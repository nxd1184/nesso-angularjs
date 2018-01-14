package vn.com.la.service.impl;

import org.springframework.data.jpa.domain.Specification;
import vn.com.la.domain.Project;
import vn.com.la.service.UserSettingService;
import vn.com.la.domain.UserSetting;
import vn.com.la.repository.UserSettingRepository;
import vn.com.la.service.dto.ProjectDTO;
import vn.com.la.service.dto.UserSettingDTO;
import vn.com.la.service.dto.param.SearchUserSettingParamDTO;
import vn.com.la.service.mapper.UserSettingMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.la.service.specification.UserSettingSpecifications;

import static vn.com.la.service.specification.ProjectSpecifications.codeContainsIgnoreCase;


/**
 * Service Implementation for managing UserSetting.
 */
@Service
@Transactional
public class UserSettingServiceImpl implements UserSettingService{

    private final Logger log = LoggerFactory.getLogger(UserSettingServiceImpl.class);

    private final UserSettingRepository userSettingRepository;

    private final UserSettingMapper userSettingMapper;

    public UserSettingServiceImpl(UserSettingRepository userSettingRepository, UserSettingMapper userSettingMapper) {
        this.userSettingRepository = userSettingRepository;
        this.userSettingMapper = userSettingMapper;
    }

    /**
     * Save a userSetting.
     *
     * @param userSettingDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public UserSettingDTO save(UserSettingDTO userSettingDTO) {
        log.debug("Request to save UserSetting : {}", userSettingDTO);
        UserSetting userSetting = userSettingMapper.toEntity(userSettingDTO);
        userSetting = userSettingRepository.save(userSetting);
        return userSettingMapper.toDto(userSetting);
    }

    /**
     *  Get all the userSettings.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UserSettingDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserSettings");
        return userSettingRepository.findAll(pageable)
            .map(userSettingMapper::toDto);
    }

    /**
     *  Get one userSetting by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public UserSettingDTO findOne(Long id) {
        log.debug("Request to get UserSetting : {}", id);
        UserSetting userSetting = userSettingRepository.findOne(id);
        return userSettingMapper.toDto(userSetting);
    }

    /**
     *  Delete the  userSetting by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserSetting : {}", id);
        userSettingRepository.delete(id);
    }

    @Override
    public Page<UserSettingDTO> search(SearchUserSettingParamDTO params, Pageable pageable) {
        Specification<UserSetting> searchSpec = UserSettingSpecifications.search(params);
        Page<UserSettingDTO> page = userSettingRepository.findAll(searchSpec,pageable).map(userSettingMapper::toDto);
        return page;
    }
}
