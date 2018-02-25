package vn.com.la.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * Created by LenVo on 2/21/18.
 */
public class ReportDTO implements Serializable{
    @NotNull
    private List<String> columns;
    @NotNull
    private List<HashMap<String, String>> rows;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public List<HashMap<String, String>> getRows() {
        return rows;
    }

    public void setRows(List<HashMap<String, String>> rows) {
        this.rows = rows;
    }
}
