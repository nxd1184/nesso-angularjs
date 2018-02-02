package vn.com.la.service.dto;

import java.time.Instant;
import java.time.ZonedDateTime;

public class CheckInReport {
    private Long userId;
    private String employee;
    private Instant day;
    private Instant checkin;
    private Instant checkout;

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public Instant getDay() {
        return day;
    }

    public void setDay(Instant day) {
        this.day = day;
    }

    public Instant getCheckin() {
        return checkin;
    }

    public void setCheckin(Instant checkin) {
        this.checkin = checkin;
    }

    public Instant getCheckout() {
        return checkout;
    }

    public void setCheckout(Instant checkout) {
        this.checkout = checkout;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
