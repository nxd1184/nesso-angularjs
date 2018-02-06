package vn.com.la.web.rest.vm.response;

import vn.com.la.service.dto.JobDTO;
import vn.com.la.service.dto.UserProductivityDTO;

import java.util.ArrayList;
import java.util.List;

public class DashboardResponseVM extends AbstractResponseVM{

    // for this week
    private Long totalReceive = 0L;
    private Long totalToDo = 0L;
    private Long totalToCheck = 0L;
    private Long totalDone = 0L;
    private Long totalRework = 0L;

    private List<JobDTO> urgentJobs = new ArrayList<>();

    // for this month
    private Long countTotalDoneForThisMonth = 0L;
    private Long countTotalDoneForLastMonth = 0L;
    private Long countTotalDoneForBestMonth = 0L;
    private List<UserProductivityDTO> userProductivityList = new ArrayList<>();


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

    public Long getCountTotalDoneForThisMonth() {
        return countTotalDoneForThisMonth;
    }

    public void setCountTotalDoneForThisMonth(Long countTotalDoneForThisMonth) {
        this.countTotalDoneForThisMonth = countTotalDoneForThisMonth;
    }

    public Long getCountTotalDoneForLastMonth() {
        return countTotalDoneForLastMonth;
    }

    public void setCountTotalDoneForLastMonth(Long countTotalDoneForLastMonth) {
        this.countTotalDoneForLastMonth = countTotalDoneForLastMonth;
    }

    public Long getCountTotalDoneForBestMonth() {
        return countTotalDoneForBestMonth;
    }

    public void setCountTotalDoneForBestMonth(Long countTotalDoneForBestMonth) {
        this.countTotalDoneForBestMonth = countTotalDoneForBestMonth;
    }

    public List<UserProductivityDTO> getUserProductivityList() {
        return userProductivityList;
    }

    public void setUserProductivityList(List<UserProductivityDTO> userProductivityList) {
        this.userProductivityList = userProductivityList;
    }

    public Long getTotalRework() {
        return totalRework;
    }

    public void setTotalRework(Long totalRework) {
        this.totalRework = totalRework;
    }
}
