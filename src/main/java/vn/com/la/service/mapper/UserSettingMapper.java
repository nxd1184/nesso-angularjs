package vn.com.la.service.mapper;

import vn.com.la.domain.*;
import vn.com.la.service.dto.UserSettingDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity UserSetting and its DTO UserSettingDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, })
public interface UserSettingMapper extends EntityMapper <UserSettingDTO, UserSetting> {

    @Mapping(source = "userConfig.id", target = "userConfigId")
    @Mapping(source = "userConfig.login", target = "userConfigLogin")
    UserSettingDTO toDto(UserSetting userSetting); 

    @Mapping(source = "userConfigId", target = "userConfig")
    UserSetting toEntity(UserSettingDTO userSettingDTO); 
    default UserSetting fromId(Long id) {
        if (id == null) {
            return null;
        }
        UserSetting userSetting = new UserSetting();
        userSetting.setId(id);
        return userSetting;
    }
}
