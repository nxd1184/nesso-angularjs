package vn.com.la.web.rest.vm.response;

import vn.com.la.service.dto.LAFolderDTO;

import java.util.ArrayList;
import java.util.List;

public class ListFolderResponseVM extends AbstractResponseVM{
    List<LAFolderDTO> directories = new ArrayList<>();

    public List<LAFolderDTO> getDirectories() {
        return directories;
    }

    public void setDirectories(List<LAFolderDTO> directories) {
        this.directories = directories;
    }
}
