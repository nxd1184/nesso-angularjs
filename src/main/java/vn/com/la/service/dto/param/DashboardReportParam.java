package vn.com.la.service.dto.param;

import java.time.ZonedDateTime;

public class DashboardReportParam {
    private ZonedDateTime fromDate;
    private ZonedDateTime toDate;

    public ZonedDateTime getFromDate() {
        return fromDate;
    }

    public void setFromDate(ZonedDateTime fromDate) {
        this.fromDate = fromDate;
    }

    public ZonedDateTime getToDate() {
        return toDate;
    }

    public void setToDate(ZonedDateTime toDate) {
        this.toDate = toDate;
    }
}
