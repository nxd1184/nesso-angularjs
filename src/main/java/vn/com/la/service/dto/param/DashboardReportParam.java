package vn.com.la.service.dto.param;

import java.time.ZonedDateTime;

public class DashboardReportParam {
    private ZonedDateTime fromDate;
    private ZonedDateTime toDate;

    private ZonedDateTime fromDealineDate;
    private ZonedDateTime toDealineDate;

    public ZonedDateTime getFromDealineDate() {
        return fromDealineDate;
    }

    public void setFromDealineDate(ZonedDateTime fromDealineDate) {
        this.fromDealineDate = fromDealineDate;
    }

    public ZonedDateTime getToDealineDate() {
        return toDealineDate;
    }

    public void setToDealineDate(ZonedDateTime toDealineDate) {
        this.toDealineDate = toDealineDate;
    }

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
