package vn.com.la.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A JobTeam.
 */
@Entity
@Table(name = "job_team")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class JobTeam implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "total_files", nullable = false)
    private Long totalFiles;

    @ManyToOne(optional = false)
    @NotNull
    private Job job;

    @ManyToOne(optional = false)
    @NotNull
    private Team team;

    @ManyToOne(optional = false)
    @NotNull
    private Project project;

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

    public JobTeam totalFiles(Long totalFiles) {
        this.totalFiles = totalFiles;
        return this;
    }

    public void setTotalFiles(Long totalFiles) {
        this.totalFiles = totalFiles;
    }

    public Job getJob() {
        return job;
    }

    public JobTeam job(Job job) {
        this.job = job;
        return this;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public Team getTeam() {
        return team;
    }

    public JobTeam team(Team team) {
        this.team = team;
        return this;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Project getProject() {
        return project;
    }

    public JobTeam project(Project project) {
        this.project = project;
        return this;
    }

    public void setProject(Project project) {
        this.project = project;
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
        JobTeam jobTeam = (JobTeam) o;
        if (jobTeam.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), jobTeam.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "JobTeam{" +
            "id=" + getId() +
            ", totalFiles='" + getTotalFiles() + "'" +
            "}";
    }
}
