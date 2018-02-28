package vn.com.la.web.rest.vm.response;

import vn.com.la.service.dto.JobTeamDTO;
import vn.com.la.service.dto.PlanProjectDTO;
import vn.com.la.service.dto.PlanTeamDTO;
import vn.com.la.service.dto.ProjectDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetAllPlanResponseVM extends AbstractResponseVM{
    private List<ProjectDTO> projects = new ArrayList<>();

    private Map<Long, PlanTeamDTO> teams = new HashMap<>();

    private Map<Long, PlanProjectDTO> timelineProjects = new HashMap<>();

    public Map<Long, PlanProjectDTO> getTimelineProjects() {
        return timelineProjects;
    }

    public void setTimelineProjects(Map<Long, PlanProjectDTO> timelineProjects) {
        this.timelineProjects = timelineProjects;
    }

    public List<ProjectDTO> getProjects() {
        return projects;
    }

    public void setProjects(List<ProjectDTO> projects) {
        this.projects = projects;
    }

    public Map<Long, PlanTeamDTO> getTeams() {
        return teams;
    }

    public void setTeams(Map<Long, PlanTeamDTO> teams) {
        this.teams = teams;
    }
}
