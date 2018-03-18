package vn.com.la.service.dto;

import java.util.HashMap;
import java.util.Map;

public class PlanTeamDTO {
    private Long teamId;
    private String teamName;

    private Map<Long, PlanUserDTO> users = new HashMap<>();

    private Long totalFiles = 0L;
    private Long totalToDo = 0L;
    private Long totalToCheck = 0L;
    private Long totalDone = 0L;
    private Long totalDelivery = 0L;

    private Long totalDoneByDays[] = new Long[7];

    public void updateByProjectViewAndStatusType(Object[] row) {

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

        Long userId = Long.parseLong(row[2].toString());
        PlanUserDTO user = null;
        if(users.containsKey(userId)) {
            user = users.get(userId);
        }else {
            user = new PlanUserDTO();
            user.setUserId(userId);
            if(row[3] != null) {
                user.setName(row[3].toString());
            }
            users.put(userId, user);
        }
        user.updateByProjectViewAndStatusType(row);

    }

    public void updateByProjectViewAndTimelineType(Object[] row) {

        for(int i = 8; i <= 14; i++) {

            if(totalDoneByDays[i - 8] == null) {
                totalDoneByDays[i - 8] = 0L;
            }

            if(row[i] != null) {
                totalDoneByDays[i - 8] += Long.parseLong(row[i].toString());
            }

            totalDone += totalDoneByDays[i - 8];
        }

        Long userId = Long.parseLong(row[6].toString());
        PlanUserDTO user = null;
        if(users.containsKey(userId)) {
            user = users.get(userId);
        }else {
            user = new PlanUserDTO();
            user.setUserId(userId);
            if(row[7] != null) {
                user.setName(row[7].toString());
            }
            users.put(userId, user);
        }
        user.updateByProjectViewAndTimelineType(row);

    }

    public void updateByUserViewAndTimelineType(Object[] row) {
        for(int i = 8; i <= 14; i++) {

            if(totalDoneByDays[i - 8] == null) {
                totalDoneByDays[i - 8] = 0L;
            }

            if(row[i] != null) {
                totalDoneByDays[i - 8] += Long.parseLong(row[i].toString());
            }

            totalDone += totalDoneByDays[i - 8];
        }

        Long userId = Long.parseLong(row[2].toString());
        PlanUserDTO user = null;
        if(users.containsKey(userId)) {
            user = users.get(userId);
        }else {
            user = new PlanUserDTO();
            user.setUserId(userId);
            if(row[3] != null) {
                user.setName(row[3].toString());
            }
            users.put(userId, user);
        }
        user.updateByUserViewAndTimelineType(row);
    }

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

    public Map<Long, PlanUserDTO> getUsers() {
        return users;
    }

    public void setUsers(Map<Long, PlanUserDTO> users) {
        this.users = users;
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

    public Long[] getTotalDoneByDays() {
        return totalDoneByDays;
    }

    public void setTotalDoneByDays(Long[] totalDoneByDays) {
        this.totalDoneByDays = totalDoneByDays;
    }
}
