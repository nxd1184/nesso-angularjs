package vn.com.la.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import vn.com.la.domain.enumeration.FileStatus;

/**
 * A DTO for the JobTeamUserTaskTracking entity.
 */
public class JobTeamUserTaskTrackingDTO implements Serializable {

    private Long id;

    @NotNull
    private FileStatus status;

    private Long userId;

    private String userLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FileStatus getStatus() {
        return status;
    }

    public void setStatus(FileStatus status) {
        this.status = status;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        JobTeamUserTaskTrackingDTO jobTeamUserTaskTrackingDTO = (JobTeamUserTaskTrackingDTO) o;
        if(jobTeamUserTaskTrackingDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), jobTeamUserTaskTrackingDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "JobTeamUserTaskTrackingDTO{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
