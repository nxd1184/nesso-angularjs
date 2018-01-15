package vn.com.la.service;

import org.apache.commons.net.ftp.FTPFile;

import java.io.File;
import java.util.List;

public interface FileSystemHandlingService {

    boolean validateProjectStructure(String projectCode);
    List<String> backLogs(String projectCode) throws Exception;
    Long countFilesFromPath(String path) throws Exception;
    void makeDirectory(String path) throws Exception;

    void copy(String from, String toPath, String fileName) throws Exception;

    List<File> listFileFromPath(String path) throws Exception;

    boolean checkFileExist(String filePath);
}
