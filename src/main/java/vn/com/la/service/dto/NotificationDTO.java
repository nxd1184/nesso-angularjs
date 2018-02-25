package vn.com.la.service.dto;

import java.time.Instant;

public class NotificationDTO {
    private String fileName;
    private Instant time;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Instant getTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }
}
