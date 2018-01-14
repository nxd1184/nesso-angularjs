package vn.com.la.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import vn.com.la.domain.enumeration.SettingTypeEnum;

/**
 * A DTO for the UserSetting entity.
 */
public class UserSettingDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private Boolean auto;

    @NotNull
    private SettingTypeEnum type;

    @NotNull
    private Boolean active;

    private Long userConfigId;

    private String userConfigName;

    private String configInfo;

    private Instant createdDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isAuto() {
        return auto;
    }

    public void setAuto(Boolean auto) {
        this.auto = auto;
    }

    public SettingTypeEnum getType() {
        return type;
    }

    public void setType(SettingTypeEnum type) {
        this.type = type;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Long getUserConfigId() {
        return userConfigId;
    }

    public void setUserConfigId(Long userId) {
        this.userConfigId = userId;
    }

    public Boolean getAuto() {
        return auto;
    }

    public Boolean getActive() {
        return active;
    }

    public String getUserConfigName() {
        return userConfigName;
    }

    public void setUserConfigName(String userConfigName) {
        this.userConfigName = userConfigName;
    }

    public String getConfigInfo() {
        return configInfo;
    }

    public void setConfigInfo(String configInfo) {
        this.configInfo = configInfo;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserSettingDTO userSettingDTO = (UserSettingDTO) o;
        if(userSettingDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), userSettingDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UserSettingDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", auto='" + isAuto() + "'" +
            ", type='" + getType() + "'" +
            ", active='" + isActive() + "'" +
            "}";
    }
}
