package vn.com.la.web.rest.vm.response;

import java.io.Serializable;

public class AbstractResponseVM implements Serializable{
    private boolean success = true;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
