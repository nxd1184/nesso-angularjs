package vn.com.la.web.rest.vm.response;

import vn.com.la.service.dto.JobDTO;

import java.util.ArrayList;
import java.util.List;

public class SyncUpProjectResponseVM extends AbstractResponseVM{
    List<JobDTO> jobs = new ArrayList<>();

    public List<JobDTO> getJobs() {
        return jobs;
    }

    public void setJobs(List<JobDTO> jobs) {
        this.jobs = jobs;
    }
}
