package vn.com.la.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Job.
 */
@Entity
@Table(name = "job")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Job implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "deadline", nullable = false)
    private ZonedDateTime deadline;

    @Column(name = "customer_requirements")
    private String customerRequirements;

    @NotNull
    @Column(name = "total_files", nullable = false)
    private Long totalFiles;

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
            "}";
    }
}
