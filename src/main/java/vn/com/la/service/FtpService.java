package vn.com.la.service;

import java.util.List;

public interface FtpService {

    boolean validateProjectStructure(String projectCode);
    List<String> backLogs(String projectCode) throws Exception;
    Long countFilesFromPath(String path) throws Exception;
}
