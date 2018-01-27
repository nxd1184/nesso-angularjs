package vn.com.la.web.rest.vm.request;

import vn.com.la.domain.enumeration.TeamStatusEnum;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

public class UpdateTeamRequestVM extends AbstractRequestVM{

    @NotNull
    private Long teamId;

    @NotNull
    private String teamName;

    private Long leaderId;

    @NotNull
    private TeamStatusEnum status;

    private List<TeamMemberRequestVM> members = new ArrayList<>();

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Long getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(Long leaderId) {
        this.leaderId = leaderId;
    }

    public TeamStatusEnum getStatus() {
        return status;
    }

    public void setStatus(TeamStatusEnum status) {
        this.status = status;
    }

    public List<TeamMemberRequestVM> getMembers() {
        return members;
    }

    public void setMembers(List<TeamMemberRequestVM> members) {
        this.members = members;
    }
}
