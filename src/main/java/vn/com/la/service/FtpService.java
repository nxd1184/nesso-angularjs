package vn.com.la.service;

import org.apache.commons.net.ftp.FTPFile;

import java.util.List;

public interface FtpService {

    boolean validateProjectStructure(String projectCode);
    List<String> backLogs(String projectCode) throws Exception;
    Long countFilesFromPath(String path) throws Exception;
    void makeDirectory(String path) throws Exception;

    void copy(String from, String toPath, String fileName) throws Exception;

    List<FTPFile> listFileFromPath(String path) throws Exception;
}
