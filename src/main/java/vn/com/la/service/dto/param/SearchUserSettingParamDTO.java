package vn.com.la.service.dto.param;

import java.time.ZonedDateTime;

public class SearchUserSettingParamDTO {
    private String name;
    private ZonedDateTime date;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }
}
