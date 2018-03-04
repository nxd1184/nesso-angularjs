package vn.com.la.service.dto;

import java.util.HashMap;
import java.util.Map;

public class PlanProjectDTO {
    private Long projectId;
    private String projectName;

    private Map<Long, PlanJobDTO> jobs = new HashMap<>();

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

        Long jobId = Long.parseLong(row[6].toString());
        PlanJobDTO job = null;
        if(jobs.containsKey(jobId)) {
            job = jobs.get(jobId);
        }else {
            job = new PlanJobDTO();
            job.setJobId(jobId);
            if(row[7] != null) {
                job.setJobName(row[7].toString());
            }
            jobs.put(jobId, job);
        }
        job.updateByProjectViewAndStatusType(row);
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

        Long jobId = Long.parseLong(row[2].toString());
        PlanJobDTO job = null;
        if(jobs.containsKey(jobId)) {
            job = jobs.get(jobId);
        }else {
            job = new PlanJobDTO();
            job.setJobId(jobId);
            if(row[3] != null) {
                job.setJobName(row[3].toString());
            }
            jobs.put(jobId, job);
        }
        job.updateByProjectViewAndTimelineType(row);
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

        Long jobId = Long.parseLong(row[6].toString());
        PlanJobDTO job = null;
        if(jobs.containsKey(jobId)) {
            job = jobs.get(jobId);
        }else {
            job = new PlanJobDTO();
            job.setJobId(jobId);
            if(row[7] != null) {
                job.setJobName(row[7].toString());
            }
            jobs.put(jobId, job);
        }
        job.updateByProjectViewAndTimelineType(row);
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Map<Long, PlanJobDTO> getJobs() {
        return jobs;
    }

    public void setJobs(Map<Long, PlanJobDTO> jobs) {
        this.jobs = jobs;
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
