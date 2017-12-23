package vn.com.la.service.dto.param;

import vn.com.la.domain.enumeration.UserStatus;
import vn.com.la.web.rest.vm.request.TeamMemberRequestVM;

import java.time.Instant;

public class TeamMemberParamDTO {
    private Long id;
    private String lastName;
    private String shift;
    private Instant startDate;
    private Instant endDate;
    private UserStatus status;

    public TeamMemberParamDTO() {}

    public TeamMemberParamDTO(TeamMemberRequestVM object) {
        if(object != null) {
            setId(object.getId());
            setLastName(object.getLastName());
            setShift(object.getShift());
            setStartDate(object.getStartDate());
            setEndDate(object.getEndDate());
            setStatus(object.getStatus());
        }

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }
}
