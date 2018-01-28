package vn.com.la.web.rest.vm.request;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

public class DashboardRequestVM extends AbstractRequestVM{

    @NotBlank
    private String fromDate;
    @NotBlank
    private String toDate;

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getToDate() {
        return toDate;
    }
}
