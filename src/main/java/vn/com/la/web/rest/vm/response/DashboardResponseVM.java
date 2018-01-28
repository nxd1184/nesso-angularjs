package vn.com.la.web.rest.vm.response;

import vn.com.la.service.dto.JobDTO;

import java.util.ArrayList;
import java.util.List;

public class DashboardResponseVM extends AbstractResponseVM{

    // for this week
    private Long totalReceive;
    private Long totalToDo;
    private Long totalToCheck;
    private Long totalDone;

    private List<JobDTO> urgentJobs = new ArrayList<>();

    // for this month
    private Long totalDoneForThisMonth;
    private Long totalDoneForLastMonth;


    public Long getTotalReceive() {
        return totalReceive;
    }

    public void setTotalReceive(Long totalReceive) {
        this.totalReceive = totalReceive;
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

    public List<JobDTO> getUrgentJobs() {
        return urgentJobs;
    }

    public void setUrgentJobs(List<JobDTO> urgentJobs) {
        this.urgentJobs = urgentJobs;
    }
}
