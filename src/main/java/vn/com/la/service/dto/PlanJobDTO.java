package vn.com.la.service.dto;

import java.util.HashMap;
import java.util.Map;

public class PlanJobDTO {
    private Long jobId;
    private String jobName;

    private Map<Long, PlanTeamDTO> teams = new HashMap<>();

    private Long totalFiles = 0L;
    private Long totalToDo = 0L;
    private Long totalToCheck = 0L;
    private Long totalDone = 0L;
    private Long totalDelivery = 0L;

    private Long totalDoneByDays[] = new Long[7];

    public void updateByProjectViewAndStatusType(Object[] row) {
        if(row[8] != null) {
            totalFiles = Long.parseLong(row[8].toString());
        }
        if(row[9] != null) {
            totalToDo = Long.parseLong(row[9].toString());
        }
        if(row[10] != null) {
            totalToCheck = Long.parseLong(row[10].toString());
        }
        if(row[11] != null) {
            totalDone = Long.parseLong(row[11].toString());
        }
        if(row[12] != null) {
            totalDelivery = Long.parseLong(row[12].toString());
        }
    }

    public void updateByProjectViewAndTimelineType(Object[] row) {
        for(int i = 8; i <= 14; i++) {

            if(totalDoneByDays[i - 8] == null) {
                totalDoneByDays[i - 8] = 0L;
            }

            Long value = 0L;
            if(row[i] != null) {
                value += Long.parseLong(row[i].toString());
            }
            totalDoneByDays[i - 8] += value;

            totalDone += value;
        }

        Long teamId = Long.parseLong(row[4].toString());
        PlanTeamDTO team = null;
        if(teams.containsKey(teamId)) {
            team = teams.get(teamId);
        }else {
            team = new PlanTeamDTO();
            team.setTeamId(teamId);
            if(row[5] != null) {
                team.setTeamName(row[5].toString());
            }
            teams.put(teamId, team);
        }
        team.updateByProjectViewAndTimelineType(row);
    }

    public void updateByUserViewAndTimelineType(Object[] row) {
        for(int i = 8; i <= 14; i++) {

            if(totalDoneByDays[i - 8] == null) {
                totalDoneByDays[i - 8] = 0L;
            }

            Long value = 0L;
            if(row[i] != null) {
                value += Long.parseLong(row[i].toString());
            }
            totalDoneByDays[i - 8] += value;

            totalDone += value;
        }
    }


    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
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

    public Map<Long, PlanTeamDTO> getTeams() {
        return teams;
    }

    public void setTeams(Map<Long, PlanTeamDTO> teams) {
        this.teams = teams;
    }

    public Long[] getTotalDoneByDays() {
        return totalDoneByDays;
    }

    public void setTotalDoneByDays(Long[] totalDoneByDays) {
        this.totalDoneByDays = totalDoneByDays;
    }
}
