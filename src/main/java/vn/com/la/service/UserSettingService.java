package vn.com.la.service;

import vn.com.la.service.dto.UserSettingDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing UserSetting.
 */
public interface UserSettingService {

    /**
     * Save a userSetting.
     *
     * @param userSettingDTO the entity to save
     * @return the persisted entity
     */
    UserSettingDTO save(UserSettingDTO userSettingDTO);

    /**
     *  Get all the userSettings.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<UserSettingDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" userSetting.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    UserSettingDTO findOne(Long id);

    /**
     *  Delete the "id" userSetting.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
