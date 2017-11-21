package vn.com.la.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import vn.com.la.domain.enumeration.SettingTypeEnum;

/**
 * A UserSetting.
 */
@Entity
@Table(name = "user_setting")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UserSetting implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "auto")
    private Boolean auto;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_type", nullable = false)
    private SettingTypeEnum type;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    @ManyToOne(optional = false)
    @NotNull
    private User userConfig;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public UserSetting name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isAuto() {
        return auto;
    }

    public UserSetting auto(Boolean auto) {
        this.auto = auto;
        return this;
    }

    public void setAuto(Boolean auto) {
        this.auto = auto;
    }

    public SettingTypeEnum getType() {
        return type;
    }

    public UserSetting type(SettingTypeEnum type) {
        this.type = type;
        return this;
    }

    public void setType(SettingTypeEnum type) {
        this.type = type;
    }

    public Boolean isActive() {
        return active;
    }

    public UserSetting active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public User getUserConfig() {
        return userConfig;
    }

    public UserSetting userConfig(User user) {
        this.userConfig = user;
        return this;
    }

    public void setUserConfig(User user) {
        this.userConfig = user;
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
        UserSetting userSetting = (UserSetting) o;
        if (userSetting.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), userSetting.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UserSetting{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", auto='" + isAuto() + "'" +
            ", type='" + getType() + "'" +
            ", active='" + isActive() + "'" +
            "}";
    }
}
