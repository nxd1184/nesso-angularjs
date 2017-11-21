package vn.com.la.service;

import vn.com.la.service.dto.SettingInfoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing SettingInfo.
 */
public interface SettingInfoService {

    /**
     * Save a settingInfo.
     *
     * @param settingInfoDTO the entity to save
     * @return the persisted entity
     */
    SettingInfoDTO save(SettingInfoDTO settingInfoDTO);

    /**
     *  Get all the settingInfos.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<SettingInfoDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" settingInfo.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    SettingInfoDTO findOne(Long id);

    /**
     *  Delete the "id" settingInfo.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
