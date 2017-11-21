package vn.com.la.service.impl;

import vn.com.la.service.SettingInfoService;
import vn.com.la.domain.SettingInfo;
import vn.com.la.repository.SettingInfoRepository;
import vn.com.la.service.dto.SettingInfoDTO;
import vn.com.la.service.mapper.SettingInfoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing SettingInfo.
 */
@Service
@Transactional
public class SettingInfoServiceImpl implements SettingInfoService{

    private final Logger log = LoggerFactory.getLogger(SettingInfoServiceImpl.class);

    private final SettingInfoRepository settingInfoRepository;

    private final SettingInfoMapper settingInfoMapper;

    public SettingInfoServiceImpl(SettingInfoRepository settingInfoRepository, SettingInfoMapper settingInfoMapper) {
        this.settingInfoRepository = settingInfoRepository;
        this.settingInfoMapper = settingInfoMapper;
    }

    /**
     * Save a settingInfo.
     *
     * @param settingInfoDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public SettingInfoDTO save(SettingInfoDTO settingInfoDTO) {
        log.debug("Request to save SettingInfo : {}", settingInfoDTO);
        SettingInfo settingInfo = settingInfoMapper.toEntity(settingInfoDTO);
        settingInfo = settingInfoRepository.save(settingInfo);
        return settingInfoMapper.toDto(settingInfo);
    }

    /**
     *  Get all the settingInfos.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SettingInfoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SettingInfos");
        return settingInfoRepository.findAll(pageable)
            .map(settingInfoMapper::toDto);
    }

    /**
     *  Get one settingInfo by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public SettingInfoDTO findOne(Long id) {
        log.debug("Request to get SettingInfo : {}", id);
        SettingInfo settingInfo = settingInfoRepository.findOne(id);
        return settingInfoMapper.toDto(settingInfo);
    }

    /**
     *  Delete the  settingInfo by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete SettingInfo : {}", id);
        settingInfoRepository.delete(id);
    }
}
