package vn.com.la.web.rest.vm.response;

import java.util.ArrayList;
import java.util.List;

public class DeliveryFilesResponseVM {
    List<String> successList = new ArrayList();
    List<String> failedList = new ArrayList();

    public List<String> getSuccessList() {
        return successList;
    }

    public void setSuccessList(List<String> successList) {
        this.successList = successList;
    }

    public List<String> getFailedList() {
        return failedList;
    }

    public void setFailedList(List<String> failedList) {
        this.failedList = failedList;
    }
}
