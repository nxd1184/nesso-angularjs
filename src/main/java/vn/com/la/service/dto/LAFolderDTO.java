package vn.com.la.service.dto;

import java.io.Serializable;

public class LAFolderDTO implements Serializable{

    private String name;
    private String relativePath;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }
}
