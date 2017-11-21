package vn.com.la.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import vn.com.la.domain.enumeration.DataTypeEnum;

/**
 * A SettingInfo.
 */
@Entity
@Table(name = "setting_info")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SettingInfo extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "jhi_key", nullable = false)
    private String key;

    @NotNull
    @Column(name = "jhi_value", nullable = false)
    private String value;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_type", nullable = false)
    private DataTypeEnum type;

    @ManyToOne(optional = false)
    @NotNull
    private UserSetting setting;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public SettingInfo key(String key) {
        this.key = key;
        return this;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public SettingInfo value(String value) {
        this.value = value;
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public DataTypeEnum getType() {
        return type;
    }

    public SettingInfo type(DataTypeEnum type) {
        this.type = type;
        return this;
    }

    public void setType(DataTypeEnum type) {
        this.type = type;
    }

    public UserSetting getSetting() {
        return setting;
    }

    public SettingInfo setting(UserSetting userSetting) {
        this.setting = userSetting;
        return this;
    }

    public void setSetting(UserSetting userSetting) {
        this.setting = userSetting;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SettingInfo settingInfo = (SettingInfo) o;
        if (settingInfo.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), settingInfo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SettingInfo{" +
            "id=" + getId() +
            ", key='" + getKey() + "'" +
            ", value='" + getValue() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
