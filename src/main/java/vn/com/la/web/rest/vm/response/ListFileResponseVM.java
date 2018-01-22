package vn.com.la.web.rest.vm.response;

import vn.com.la.service.dto.LAFileDTO;

import java.util.ArrayList;
import java.util.List;

public class ListFileResponseVM extends AbstractResponseVM{
    List<LAFileDTO> files = new ArrayList<>();

    public List<LAFileDTO> getFiles() {
        return files;
    }

    public void setFiles(List<LAFileDTO> files) {
        this.files = files;
    }
}
