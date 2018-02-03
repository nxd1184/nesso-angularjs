package vn.com.la.service.dto;

import java.util.HashMap;
import java.util.Map;

public class PlanUserDTO {
    private Long userId;
    private String name;

    private Map<Long, PlanProjectDTO> projects = new HashMap<>();

    private Long totalFiles = 0L;
    private Long totalToDo = 0L;
    private Long totalToCheck = 0L;
    private Long totalDone = 0L;
    private Long totalDelivery = 0L;

    public void update(Object[] row) {

        if(row[8] != null) {
            totalFiles += Long.parseLong(row[8].toString());
        }
        if(row[9] != null) {
            totalToDo += Long.parseLong(row[9].toString());
        }
        if(row[10] != null) {
            totalToCheck += Long.parseLong(row[10].toString());
        }
        if(row[11] != null) {
            totalDone += Long.parseLong(row[11].toString());
        }
        if(row[12] != null) {
            totalDelivery += Long.parseLong(row[12].toString());
        }

        PlanProjectDTO project = null;
        Long projectId = Long.parseLong(row[4].toString());
        if(projects.containsKey(projectId)) {
            project = projects.get(projectId);
        }else {
            project = new PlanProjectDTO();
            project.setProjectId(projectId);
            if(row[5] != null) {
                project.setProjectName(row[5].toString());
            }
            projects.put(projectId, project);
        }
        project.update(row);
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<Long, PlanProjectDTO> getProjects() {
        return projects;
    }

    public void setProjects(Map<Long, PlanProjectDTO> projects) {
        this.projects = projects;
    }

    public Long getTotalToDo() {
        return totalToDo;
    }

    public void setTotalToDo(Long totalToDo) {
        this.totalToDo = totalToDo;
    }

    public Long getTotalToCheck() {
        return totalToCheck;
    }

    public void setTotalToCheck(Long totalToCheck) {
        this.totalToCheck = totalToCheck;
    }

    public Long getTotalDone() {
        return totalDone;
    }

    public void setTotalDone(Long totalDone) {
        this.totalDone = totalDone;
    }

    public Long getTotalDelivery() {
        return totalDelivery;
    }

    public void setTotalDelivery(Long totalDelivery) {
        this.totalDelivery = totalDelivery;
    }

    public Long getTotalFiles() {
        return totalFiles;
    }

    public void setTotalFiles(Long totalFiles) {
        this.totalFiles = totalFiles;
    }
}
