package vn.com.la.service.mapper;

import vn.com.la.domain.*;
import vn.com.la.service.dto.SettingInfoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity SettingInfo and its DTO SettingInfoDTO.
 */
@Mapper(componentModel = "spring", uses = {UserSettingMapper.class, })
public interface SettingInfoMapper extends EntityMapper <SettingInfoDTO, SettingInfo> {

    @Mapping(source = "setting.id", target = "settingId")
    @Mapping(source = "setting.name", target = "settingName")
    SettingInfoDTO toDto(SettingInfo settingInfo); 

    @Mapping(source = "settingId", target = "setting")
    SettingInfo toEntity(SettingInfoDTO settingInfoDTO); 
    default SettingInfo fromId(Long id) {
        if (id == null) {
            return null;
        }
        SettingInfo settingInfo = new SettingInfo();
        settingInfo.setId(id);
        return settingInfo;
    }
}
