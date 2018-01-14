package vn.com.la.web.rest.vm.response;

import org.springframework.data.domain.Page;

import java.util.List;

public class DatatableResponseVM extends AbstractResponseVM{
    private Long recordsTotal;
    private Long recordsFiltered;
    private Object data;

    public DatatableResponseVM() {}

    public DatatableResponseVM(Page page) {
        this.setData(page.getContent());
        this.setRecordsFiltered(page.getTotalElements());
        this.setRecordsTotal(page.getTotalElements());
    }

    public Long getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(Long recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public Long getRecordsFiltered() {
        return recordsFiltered;
    }

    public void setRecordsFiltered(Long recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
