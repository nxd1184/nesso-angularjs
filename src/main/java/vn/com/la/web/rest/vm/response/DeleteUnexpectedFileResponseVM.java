package vn.com.la.web.rest.vm.response;

import java.util.ArrayList;
import java.util.List;

/**
 * @author steven on 4/22/18
 * @project nesso-angularjs
 */
public class DeleteUnexpectedFileResponseVM extends AbstractResponseVM {
    private List<String> undeletedFiles = new ArrayList<>();

    public List<String> getUndeletedFiles() {
        return undeletedFiles;
    }

    public void setUndeletedFiles(List<String> undeletedFiles) {
        this.undeletedFiles = undeletedFiles;
    }
}
