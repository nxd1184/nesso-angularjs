package vn.com.la.service.dto.param;

import vn.com.la.domain.enumeration.TeamStatusEnum;

import java.util.ArrayList;
import java.util.List;

public class UpdateTeamParamDTO {
    private Long teamId;
    private String teamName;
    private Long leaderId;
    private TeamStatusEnum status;

    private List<TeamMemberParamDTO> members = new ArrayList<>();

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

    public List<TeamMemberParamDTO> getMembers() {
        return members;
    }

    public void setMembers(List<TeamMemberParamDTO> members) {
        this.members = members;
    }
}
