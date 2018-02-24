package vn.com.la.service.dto.param;

import org.joda.time.DateTime;

public class SubmitTimesheetParam {

    private Long userId;
    private DateTime date;
    private DateTime time;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }

    public DateTime getTime() {
        return time;
    }

    public void setTime(DateTime time) {
        this.time = time;
    }
}
