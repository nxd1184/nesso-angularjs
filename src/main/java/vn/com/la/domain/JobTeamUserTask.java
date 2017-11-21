package vn.com.la.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

import vn.com.la.domain.enumeration.FileStatusEnum;

/**
 * A JobTeamUserTask.
 */
@Entity
@Table(name = "job_team_user_task")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class JobTeamUserTask implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "original_file_name", nullable = false)
    private String originalFileName;

    @NotNull
    @Column(name = "original_file_path", nullable = false)
    private String originalFilePath;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private FileStatusEnum status;

    @NotNull
    @Column(name = "file_name", nullable = false)
    private String fileName;

    @NotNull
    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "number_of_rework")
    private Integer numberOfRework;

    @Column(name = "last_check_in_time")
    private ZonedDateTime lastCheckInTime;

    @Column(name = "qc_edit")
    private Boolean qcEdit;

    @Column(name = "rework")
    private Boolean rework;

    @ManyToOne(optional = false)
    @NotNull
    private JobTeamUser jobTeamUser;

    @ManyToOne(optional = false)
    @NotNull
    private User assignee;

    @ManyToOne
    private User qc;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public JobTeamUserTask originalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
        return this;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getOriginalFilePath() {
        return originalFilePath;
    }

    public JobTeamUserTask originalFilePath(String originalFilePath) {
        this.originalFilePath = originalFilePath;
        return this;
    }

    public void setOriginalFilePath(String originalFilePath) {
        this.originalFilePath = originalFilePath;
    }

    public FileStatusEnum getStatus() {
        return status;
    }

    public JobTeamUserTask status(FileStatusEnum status) {
        this.status = status;
        return this;
    }

    public void setStatus(FileStatusEnum status) {
        this.status = status;
    }

    public String getFileName() {
        return fileName;
    }

    public JobTeamUserTask fileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public JobTeamUserTask filePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Integer getNumberOfRework() {
        return numberOfRework;
    }

    public JobTeamUserTask numberOfRework(Integer numberOfRework) {
        this.numberOfRework = numberOfRework;
        return this;
    }

    public void setNumberOfRework(Integer numberOfRework) {
        this.numberOfRework = numberOfRework;
    }

    public ZonedDateTime getLastCheckInTime() {
        return lastCheckInTime;
    }

    public JobTeamUserTask lastCheckInTime(ZonedDateTime lastCheckInTime) {
        this.lastCheckInTime = lastCheckInTime;
        return this;
    }

    public void setLastCheckInTime(ZonedDateTime lastCheckInTime) {
        this.lastCheckInTime = lastCheckInTime;
    }

    public Boolean isQcEdit() {
        return qcEdit;
    }

    public JobTeamUserTask qcEdit(Boolean qcEdit) {
        this.qcEdit = qcEdit;
        return this;
    }

    public void setQcEdit(Boolean qcEdit) {
        this.qcEdit = qcEdit;
    }

    public Boolean isRework() {
        return rework;
    }

    public JobTeamUserTask rework(Boolean rework) {
        this.rework = rework;
        return this;
    }

    public void setRework(Boolean rework) {
        this.rework = rework;
    }

    public JobTeamUser getJobTeamUser() {
        return jobTeamUser;
    }

    public JobTeamUserTask jobTeamUser(JobTeamUser jobTeamUser) {
        this.jobTeamUser = jobTeamUser;
        return this;
    }

    public void setJobTeamUser(JobTeamUser jobTeamUser) {
        this.jobTeamUser = jobTeamUser;
    }

    public User getAssignee() {
        return assignee;
    }

    public JobTeamUserTask assignee(User user) {
        this.assignee = user;
        return this;
    }

    public void setAssignee(User user) {
        this.assignee = user;
    }

    public User getQc() {
        return qc;
    }

    public JobTeamUserTask qc(User user) {
        this.qc = user;
        return this;
    }

    public void setQc(User user) {
        this.qc = user;
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
        JobTeamUserTask jobTeamUserTask = (JobTeamUserTask) o;
        if (jobTeamUserTask.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), jobTeamUserTask.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "JobTeamUserTask{" +
            "id=" + getId() +
            ", originalFileName='" + getOriginalFileName() + "'" +
            ", originalFilePath='" + getOriginalFilePath() + "'" +
            ", status='" + getStatus() + "'" +
            ", fileName='" + getFileName() + "'" +
            ", filePath='" + getFilePath() + "'" +
            ", numberOfRework='" + getNumberOfRework() + "'" +
            ", lastCheckInTime='" + getLastCheckInTime() + "'" +
            ", qcEdit='" + isQcEdit() + "'" +
            ", rework='" + isRework() + "'" +
            "}";
    }
}
