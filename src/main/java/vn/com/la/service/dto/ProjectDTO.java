package vn.com.la.service.dto;


import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import vn.com.la.domain.enumeration.ProjectTypeEnum;
import vn.com.la.domain.enumeration.ProjectStatusEnum;

/**
 * A DTO for the Project entity.
 */
public class ProjectDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String code;

    @NotNull
    private String customer;

    @NotNull
    private String note;

    @NotNull
    private ProjectTypeEnum type;

    @NotNull
    private ZonedDateTime startDate;

    @NotNull
    private ProjectStatusEnum status;

    private Long managerId;

    private String managerName;

    private String managerLogin;

    private Set<JobDTO> jobs = new HashSet<>();

    private Long totalToDoFiles;
    private Long totalToCheckFiles;
    private Long totalDoneFiles;
    private Long totalDeliveryFiles;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public ProjectTypeEnum getType() {
        return type;
    }

    public void setType(ProjectTypeEnum type) {
        this.type = type;
    }

    public ZonedDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public ProjectStatusEnum getStatus() {
        return status;
    }

    public void setStatus(ProjectStatusEnum status) {
        this.status = status;
    }

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long userId) {
        this.managerId = userId;
    }

    public String getManagerLogin() {
        return managerLogin;
    }

    public void setManagerLogin(String userLogin) {
        this.managerLogin = userLogin;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public Set<JobDTO> getJobs() {
        return jobs;
    }

    public void setJobs(Set<JobDTO> jobs) {
        this.jobs = jobs;
    }

    public void addJob(JobDTO job) {
        this.jobs.add(job);
    }

    public Long getTotalToDoFiles() {
        return totalToDoFiles;
    }

    public void setTotalToDoFiles(Long totalToDoFiles) {
        this.totalToDoFiles = totalToDoFiles;
    }

    public Long getTotalToCheckFiles() {
        return totalToCheckFiles;
    }

    public void setTotalToCheckFiles(Long totalToCheckFiles) {
        this.totalToCheckFiles = totalToCheckFiles;
    }

    public Long getTotalDoneFiles() {
        return totalDoneFiles;
    }

    public void setTotalDoneFiles(Long totalDoneFiles) {
        this.totalDoneFiles = totalDoneFiles;
    }

    public Long getTotalDeliveryFiles() {
        return totalDeliveryFiles;
    }

    public void setTotalDeliveryFiles(Long totalDeliveryFiles) {
        this.totalDeliveryFiles = totalDeliveryFiles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProjectDTO projectDTO = (ProjectDTO) o;
        if(projectDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), projectDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProjectDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", code='" + getCode() + "'" +
            ", customer='" + getCustomer() + "'" +
            ", note='" + getNote() + "'" +
            ", type='" + getType() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
