package vn.com.la.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import vn.com.la.domain.enumeration.DataTypeEnum;

/**
 * A DTO for the SettingInfo entity.
 */
public class SettingInfoDTO implements Serializable {

    private Long id;

    @NotNull
    private String key;

    @NotNull
    private String value;

    @NotNull
    private DataTypeEnum type;

    private Long settingId;

    private String settingName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public DataTypeEnum getType() {
        return type;
    }

    public void setType(DataTypeEnum type) {
        this.type = type;
    }

    public Long getSettingId() {
        return settingId;
    }

    public void setSettingId(Long userSettingId) {
        this.settingId = userSettingId;
    }

    public String getSettingName() {
        return settingName;
    }

    public void setSettingName(String userSettingName) {
        this.settingName = userSettingName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SettingInfoDTO settingInfoDTO = (SettingInfoDTO) o;
        if(settingInfoDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), settingInfoDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SettingInfoDTO{" +
            "id=" + getId() +
            ", key='" + getKey() + "'" +
            ", value='" + getValue() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
