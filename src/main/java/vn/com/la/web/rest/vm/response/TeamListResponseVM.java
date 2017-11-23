package vn.com.la.web.rest.vm.response;

import vn.com.la.service.dto.TeamDTO;

import java.util.List;

public class TeamListResponseVM extends AbstractResponseVM{
    private List<TeamDTO> teams;

    public List<TeamDTO> getTeams() {
        return teams;
    }

    public void setTeams(List<TeamDTO> teams) {
        this.teams = teams;
    }
}
