package vn.com.la.service.dto;

import java.time.ZonedDateTime;

public class CheckInReport {
    private Long userId;
    private String employee;
    private ZonedDateTime day;
    private ZonedDateTime checkin;
    private ZonedDateTime checkout;

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public ZonedDateTime getDay() {
        return day;
    }

    public void setDay(ZonedDateTime day) {
        this.day = day;
    }

    public ZonedDateTime getCheckin() {
        return checkin;
    }

    public void setCheckin(ZonedDateTime checkin) {
        this.checkin = checkin;
    }

    public ZonedDateTime getCheckout() {
        return checkout;
    }

    public void setCheckout(ZonedDateTime checkout) {
        this.checkout = checkout;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
