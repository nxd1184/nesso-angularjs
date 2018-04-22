package vn.com.la.web.rest.vm.response;

import java.util.ArrayList;
import java.util.List;

/**
 * @author steven on 4/22/18
 * @project nesso-angularjs
 */
public class DeleteUnexpectedFileResponseVM extends AbstractResponseVM {
    private List<String> failedList = new ArrayList<>();

    public List<String> getFailedList() {
        return failedList;
    }

    public void setFailedList(List<String> failedList) {
        this.failedList = failedList;
    }
}
