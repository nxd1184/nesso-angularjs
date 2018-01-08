package vn.com.la.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.com.la.domain.enumeration.JobStatusEnum;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Job.
 */
@Entity
@Table(name = "job")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Job extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;


    @Column(name = "deadline", nullable = true)
    private ZonedDateTime deadline;

    @Column(name = "customer_requirements")
    private String customerRequirements;

    @Column(name = "total_files", nullable = true)
    private Long totalFiles;

    @ManyToOne(optional = false)
    @NotNull
    private Project project;

    @OneToMany(mappedBy = "job")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<JobTeam> jobTeams = new HashSet<>();

    @OneToMany(mappedBy = "job")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<JobTask> jobTasks = new HashSet<>();

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "default 'ACTIVE'")
    private JobStatusEnum status;

    @Column(name = "started")
    private Boolean started = false;

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

    public Job name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ZonedDateTime getDeadline() {
        return deadline;
    }

    public Job deadline(ZonedDateTime deadline) {
        this.deadline = deadline;
        return this;
    }

    public void setDeadline(ZonedDateTime deadline) {
        this.deadline = deadline;
    }

    public String getCustomerRequirements() {
        return customerRequirements;
    }

    public Job customerRequirements(String customerRequirements) {
        this.customerRequirements = customerRequirements;
        return this;
    }

    public void setCustomerRequirements(String customerRequirements) {
        this.customerRequirements = customerRequirements;
    }

    public Long getTotalFiles() {
        return totalFiles;
    }

    public Job totalFiles(Long totalFiles) {
        this.totalFiles = totalFiles;
        return this;
    }

    public void setTotalFiles(Long totalFiles) {
        this.totalFiles = totalFiles;
    }

    public Project getProject() {
        return project;
    }

    public Job project(Project project) {
        this.project = project;
        return this;
    }

    public void setProject(Project project) {
        this.project = project;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove


    public Set<JobTeam> getJobTeams() {
        return jobTeams;
    }

    public void setJobTeams(Set<JobTeam> jobTeams) {
        this.jobTeams = jobTeams;
    }

    public Set<JobTask> getJobTasks() {
        return jobTasks;
    }

    public void setJobTasks(Set<JobTask> jobTasks) {
        this.jobTasks = jobTasks;
    }

    public Boolean getStarted() {
        return started;
    }

    public void setStarted(Boolean started) {
        this.started = started;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Job job = (Job) o;
        if (job.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), job.getId());
    }

    public JobStatusEnum getStatus() {
        return status;
    }

    public void setStatus(JobStatusEnum status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Job{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", deadline='" + getDeadline() + "'" +
            ", customerRequirements='" + getCustomerRequirements() + "'" +
            ", totalFiles='" + getTotalFiles() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
