package vn.com.la.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the JobTeamUser entity.
 */
public class JobTeamUserDTO implements Serializable {

    private Long id;

    @NotNull
    private Long totalFiles;

    private Long jobTeamId;

    private Long userId;

    private String userLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTotalFiles() {
        return totalFiles;
    }

    public void setTotalFiles(Long totalFiles) {
        this.totalFiles = totalFiles;
    }

    public Long getJobTeamId() {
        return jobTeamId;
    }

    public void setJobTeamId(Long jobTeamId) {
        this.jobTeamId = jobTeamId;
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

        JobTeamUserDTO jobTeamUserDTO = (JobTeamUserDTO) o;
        if(jobTeamUserDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), jobTeamUserDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "JobTeamUserDTO{" +
            "id=" + getId() +
            ", totalFiles='" + getTotalFiles() + "'" +
            "}";
    }
}
