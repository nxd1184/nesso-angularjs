package vn.com.la.web.rest.vm.request;

import java.util.ArrayList;
import java.util.List;

public class DeliveryFilesRequestVM extends AbstractRequestVM {
    List<String> fileNames = new ArrayList();

    public List<String> getFileNames() {
        return fileNames;
    }

    public void setFileNames(List<String> fileNames) {
        this.fileNames = fileNames;
    }
}
