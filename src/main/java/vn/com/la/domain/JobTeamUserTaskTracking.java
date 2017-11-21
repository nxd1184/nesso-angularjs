package vn.com.la.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import vn.com.la.domain.enumeration.FileStatus;

/**
 * A JobTeamUserTaskTracking.
 */
@Entity
@Table(name = "job_team_user_task_tracking")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class JobTeamUserTaskTracking extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private FileStatus status;

    @ManyToOne(optional = false)
    @NotNull
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FileStatus getStatus() {
        return status;
    }

    public JobTeamUserTaskTracking status(FileStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(FileStatus status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public JobTeamUserTaskTracking user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
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
        JobTeamUserTaskTracking jobTeamUserTaskTracking = (JobTeamUserTaskTracking) o;
        if (jobTeamUserTaskTracking.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), jobTeamUserTaskTracking.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "JobTeamUserTaskTracking{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
