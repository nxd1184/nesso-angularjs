package vn.com.la.service;

import org.apache.commons.net.ftp.FTPFile;
import vn.com.la.web.rest.vm.response.ListFileResponseVM;
import vn.com.la.web.rest.vm.response.ListFolderResponseVM;

import java.io.File;
import java.util.List;

public interface FileSystemHandlingService {

    boolean validateProjectStructure(String projectCode);
    List<String> backLogs(String projectCode) throws Exception;
    Long countFilesFromPath(String path) throws Exception;
    void makeDirectory(String path) throws Exception;

    void copy(String from, String toPath, String fileName) throws Exception;
    void copy(String fromSrouce, String toSource) throws Exception;

    List<File> listFileFromPath(String path) throws Exception;

    List<File> listFileRecursiveFromPath(String path) throws Exception;

    boolean checkFileExist(String filePath);

    ListFolderResponseVM listNfsFolderFromPath(String path);
    ListFileResponseVM listNfsFileFromPath(String path);
}
