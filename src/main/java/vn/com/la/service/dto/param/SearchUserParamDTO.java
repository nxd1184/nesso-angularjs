package vn.com.la.service.dto.param;

import vn.com.la.domain.Authority;

import java.util.List;
import java.util.Set;

public class SearchUserParamDTO {
    private String searchTerms;
    private List<String> authorities;

    public String getSearchTerms() {
        return searchTerms;
    }

    public void setSearchTerms(String searchTerms) {
        this.searchTerms = searchTerms;
    }

    public List<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<String> authorities) {
        this.authorities = authorities;
    }
}
