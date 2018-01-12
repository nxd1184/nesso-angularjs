package vn.com.la.web.rest.vm.request;

import javax.validation.constraints.NotNull;

public class CheckInJobTeamUserTaskRequestVM extends AbstractRequestVM{
    @NotNull
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
