package vn.com.la.web.rest.vm.response;

import java.util.ArrayList;
import java.util.List;

public class CheckInAllResponseVM extends AbstractResponseVM{
    private List<Long> ids = new ArrayList<>();

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }
}
