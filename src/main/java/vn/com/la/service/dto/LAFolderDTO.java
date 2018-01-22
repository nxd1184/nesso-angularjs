package vn.com.la.service.dto;

import java.io.Serializable;

public class LAFolderDTO implements Serializable{

    private String name;
    private String fullPath;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }
}
