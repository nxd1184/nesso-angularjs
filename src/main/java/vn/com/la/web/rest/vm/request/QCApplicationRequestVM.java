package vn.com.la.web.rest.vm.request;

import org.hibernate.validator.constraints.NotEmpty;

public class QCApplicationRequestVM extends AbstractRequestVM{

    @NotEmpty
    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
