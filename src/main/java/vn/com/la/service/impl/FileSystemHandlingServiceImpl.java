package vn.com.la.service.impl;

import com.google.common.io.Files;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.stereotype.Service;
import vn.com.la.config.ApplicationProperties;
import vn.com.la.config.Constants;
import vn.com.la.config.audit.FtpProperties;
import vn.com.la.service.FileSystemHandlingService;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class FileSystemHandlingServiceImpl implements FileSystemHandlingService {
    private final ApplicationProperties applicationProperties;

    private String rootFolder;

    private FileSystemHandlingServiceImpl(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
        this.rootFolder = this.applicationProperties.getRootFolder();
    }

    @Override
    public boolean validateProjectStructure(String projectCode) {

        try {

            File file = new File(rootFolder + Constants.DASH + projectCode);

            if(!file.exists()) {
                return false;
            }

            String[] projectFolders = {Constants.BACK_LOGS, Constants.TO_DO, Constants.TO_CHECK, Constants.DONE, Constants.DELIVERY};
            for(String folder: projectFolders) {
                File projectFolder = new File(rootFolder + Constants.DASH + projectCode + Constants.DASH + folder);
                if(!projectFolder.exists()) {
                    return false;
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public List<String> backLogs(String projectCode) throws Exception{

        List<String> folderNames = new ArrayList<>();

        File projectFolder = new File(rootFolder + Constants.DASH + projectCode + Constants.DASH + Constants.BACK_LOGS);

        for(File backlogItem: projectFolder.listFiles()) {
            if(backlogItem.isDirectory()) {
                folderNames.add(backlogItem.getName());
            }
        }

        return folderNames;
    }

    @Override
    public Long countFilesFromPath(String path) throws Exception{
        Long totalFiles = 0L;

        File file = new File(rootFolder + Constants.DASH + path);
        totalFiles = new Long(file.list().length);

        return totalFiles;
    }

    @Override
    public void makeDirectory(String path) throws Exception {
        File newDir = new File(rootFolder + Constants.DASH + path);
        newDir.createNewFile();
    }

    @Override
    public void copy(String fromSource, String toPath, String fileName) throws Exception {
        Files.copy(new File( rootFolder + Constants.DASH +fromSource), new File(rootFolder + Constants.DASH + toPath + Constants.DASH + fileName));
    }

    @Override
    public List<File> listFileFromPath(String path) throws Exception {
        File file = new File(rootFolder + Constants.DASH + path);
        return Arrays.asList(file.listFiles());
    }

    @Override
    public boolean checkFileExist(String filePath) {
        File file = new File(rootFolder + Constants.DASH + filePath);

        return file.exists();
    }
}
