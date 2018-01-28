package vn.com.la.web.rest.vm.request;

import java.time.ZonedDateTime;

public class DashboardRequestVM extends AbstractRequestVM{

    private String fromDate;
    private String toDate;

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getToDate() {
        return toDate;
    }
}
