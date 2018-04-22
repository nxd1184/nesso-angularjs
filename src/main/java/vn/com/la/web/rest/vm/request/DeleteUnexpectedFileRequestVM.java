package vn.com.la.web.rest.vm.request;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author steven on 4/22/18
 * @project nesso-angularjs
 */
public class DeleteUnexpectedFileRequestVM extends AbstractRequestVM {
    @NotNull
    private List<String> deletedFiles = new ArrayList<>();

    public List<String> getDeletedFiles() {
        return deletedFiles;
    }

    public void setDeletedFiles(List<String> deletedFiles) {
        this.deletedFiles = deletedFiles;
    }
}
