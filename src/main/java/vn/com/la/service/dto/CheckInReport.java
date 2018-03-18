package vn.com.la.service.dto;

import java.time.Instant;
import java.time.ZonedDateTime;

public class CheckInReport {
    private Long userId;
    private String employee;
    private Instant day;
    private Instant checkin;
    private Instant checkout;
    private Instant checkin_overtime;
    private Instant checkout_overtime;

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

    public Instant getCheckin_overtime() {
        return checkin_overtime;
    }

    public void setCheckin_overtime(Instant checkin_overtime) {
        this.checkin_overtime = checkin_overtime;
    }

    public Instant getCheckout_overtime() {
        return checkout_overtime;
    }

    public void setCheckout_overtime(Instant checkout_overtime) {
        this.checkout_overtime = checkout_overtime;
    }
}
