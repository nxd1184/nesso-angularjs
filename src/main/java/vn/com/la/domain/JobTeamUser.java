package vn.com.la.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A JobTeamUser.
 */
@Entity
@Table(name = "job_team_user")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class JobTeamUser extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "total_files", nullable = false)
    private Long totalFiles;

    @ManyToOne(optional = false)
    @NotNull
    private JobTeam jobTeam;

    @ManyToOne(optional = false)
    @NotNull
    private User user;

    @OneToMany(mappedBy = "jobTeamUser", cascade = CascadeType.ALL, orphanRemoval=true)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<JobTeamUserTask> jobTeamUserTasks = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTotalFiles() {
        return totalFiles;
    }

    public JobTeamUser totalFiles(Long totalFiles) {
        this.totalFiles = totalFiles;
        return this;
    }

    public void setTotalFiles(Long totalFiles) {
        this.totalFiles = totalFiles;
    }

    public JobTeam getJobTeam() {
        return jobTeam;
    }

    public JobTeamUser jobTeam(JobTeam jobTeam) {
        this.jobTeam = jobTeam;
        return this;
    }

    public void setJobTeam(JobTeam jobTeam) {
        this.jobTeam = jobTeam;
    }

    public User getUser() {
        return user;
    }

    public JobTeamUser user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove


    public Set<JobTeamUserTask> getJobTeamUserTasks() {
        return jobTeamUserTasks;
    }

    public void setJobTeamUserTasks(Set<JobTeamUserTask> jobTeamUserTasks) {
        this.jobTeamUserTasks = jobTeamUserTasks;
    }

    public Set<String> getAuthorities() {
        return user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toSet());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        JobTeamUser jobTeamUser = (JobTeamUser) o;
        if (jobTeamUser.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), jobTeamUser.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "JobTeamUser{" +
            "id=" + getId() +
            ", totalFiles='" + getTotalFiles() + "'" +
            "}";
    }
}
