package vn.com.la.service.dto;

import java.time.ZonedDateTime;

public class DeliveryQualityReportDTO {
    private String employee;
    private String projectName;
    private String jobName;
    private Long volumn;
    private Long done;
    private Long error;
    private Double error_rate;
    private ZonedDateTime received_date;

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public Long getVolumn() {
        return volumn;
    }

    public void setVolumn(Long volumn) {
        this.volumn = volumn;
    }

    public Long getDone() {
        return done;
    }

    public void setDone(Long done) {
        this.done = done;
    }

    public Long getError() {
        return error;
    }

    public void setError(Long error) {
        this.error = error;
    }

    public Double getError_rate() {
        return error_rate;
    }

    public void setError_rate(Double error_rate) {
        this.error_rate = error_rate;
    }

    public ZonedDateTime getReceived_date() {
        return received_date;
    }

    public void setReceived_date(ZonedDateTime received_date) {
        this.received_date = received_date;
    }
}
