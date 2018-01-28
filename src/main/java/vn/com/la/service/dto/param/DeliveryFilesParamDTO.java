package vn.com.la.service.dto.param;

import java.util.ArrayList;
import java.util.List;

public class DeliveryFilesParamDTO {
    private List<String> fileNames = new ArrayList<>();

    public List<String> getFileNames() {
        return fileNames;
    }

    public void setFileNames(List<String> fileNames) {
        this.fileNames = fileNames;
    }
}
