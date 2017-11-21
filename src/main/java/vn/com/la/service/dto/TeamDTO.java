package vn.com.la.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import vn.com.la.domain.enumeration.TeamStatusEnum;

/**
 * A DTO for the Team entity.
 */
public class TeamDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private TeamStatusEnum status;

    private Long leaderId;

    private String leaderLogin;

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

    public TeamStatusEnum getStatus() {
        return status;
    }

    public void setStatus(TeamStatusEnum status) {
        this.status = status;
    }

    public Long getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(Long userId) {
        this.leaderId = userId;
    }

    public String getLeaderLogin() {
        return leaderLogin;
    }

    public void setLeaderLogin(String userLogin) {
        this.leaderLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TeamDTO teamDTO = (TeamDTO) o;
        if(teamDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), teamDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TeamDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
