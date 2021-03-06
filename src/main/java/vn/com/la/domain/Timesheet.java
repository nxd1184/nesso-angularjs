package vn.com.la.domain;

import org.joda.time.DateTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "timesheet")
public class Timesheet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "check_in_time")
    private Date checkInTime;

    @Column(name = "check_out_time")
    private Date checkOutTime;

    @Column(name = "check_in_over_time")
    private Date checkInOverTime;

    @Column(name = "check_out_over_time")
    private Date checkOutOverTime;

    @NotNull
    @ManyToOne
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(Date checkInTime) {
        this.checkInTime = checkInTime;
    }

    public Date getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(Date checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getCheckInOverTime() {
        return checkInOverTime;
    }

    public void setCheckInOverTime(Date checkInOverTime) {
        this.checkInOverTime = checkInOverTime;
    }

    public Date getCheckOutOverTime() {
        return checkOutOverTime;
    }

    public void setCheckOutOverTime(Date checkOutOverTime) {
        this.checkOutOverTime = checkOutOverTime;
    }
}
