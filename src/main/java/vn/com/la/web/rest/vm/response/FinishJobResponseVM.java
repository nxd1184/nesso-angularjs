package vn.com.la.web.rest.vm.response;

/**
 * @author steven on 4/22/18
 * @project nesso-angularjs
 */
public class FinishJobResponseVM extends AbstractResponseVM {
    private long totalUnDoneTasks = -1;

    public long getTotalUnDoneTasks() {
        return totalUnDoneTasks;
    }

    public void setTotalUnDoneTasks(long totalUnDoneTasks) {
        this.totalUnDoneTasks = totalUnDoneTasks;
    }
}
