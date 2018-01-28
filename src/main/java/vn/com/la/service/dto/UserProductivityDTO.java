package vn.com.la.service.dto;

import java.io.Serializable;

public class UserProductivityDTO implements Serializable{
    private String name;
    private Long totalCredit = 0L;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTotalCredit() {
        return totalCredit;
    }

    public void setTotalCredit(Long totalCredit) {
        this.totalCredit = totalCredit;
    }
}
