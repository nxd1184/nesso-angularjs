package vn.com.la.service.impl;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.stereotype.Service;
import vn.com.la.config.ApplicationProperties;
import vn.com.la.config.Constants;
import vn.com.la.config.audit.FtpProperties;
import vn.com.la.service.FtpService;

import java.util.ArrayList;
import java.util.List;

@Service
public class FtpServiceImpl implements FtpService{
    private final ApplicationProperties applicationProperties;

    private FTPClient ftpClient;

    private FtpServiceImpl(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;

        FtpProperties ftpProperties = applicationProperties.getFtpProperties();
        this.ftpClient = new FTPClient();
        try {
            this.ftpClient.connect(ftpProperties.getHost());
            int reply = this.ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                this.ftpClient.disconnect();
                throw new Exception("Exception in connecting to FTP Server");
            }
            this.ftpClient.login(ftpProperties.getUsername(), ftpProperties.getPassword());
            this.ftpClient.enterLocalPassiveMode();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean validateProjectStructure(String projectCode) {

        try {
            ftpClient.changeWorkingDirectory(Constants.DASH + projectCode);
            int returnCode = ftpClient.getReplyCode();
            if (returnCode == 550) {
                return false;
            }

            String[] projectFolders = {Constants.BACK_LOGS, Constants.TO_DO, Constants.TO_CHECK, Constants.DONE, Constants.DELIVERY};
            for(String folder: projectFolders) {
                ftpClient.changeWorkingDirectory(Constants.DASH + projectCode + Constants.DASH + folder);
                returnCode = ftpClient.getReplyCode();
                if (returnCode == 550) {
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

        ftpClient.changeWorkingDirectory("/" + projectCode + "/" + Constants.BACK_LOGS);
        int returnCode = ftpClient.getReplyCode();
        if (returnCode == 550) {
            throw new Exception("Backlogs not found");
        }

        for(FTPFile file: ftpClient.listDirectories()) {
            if(file.isDirectory()) {
                folderNames.add(file.getName());
            }
        }

        return folderNames;
    }

    @Override
    public Long countFilesFromPath(String path) throws Exception{
        Long totalFiles = 0L;
        ftpClient.changeWorkingDirectory(path);
        FTPFile[] files = ftpClient.listFiles();
        for(FTPFile file: files) {
            if(file.isFile()) {
                totalFiles++;
            }else {
                totalFiles += countFilesFromPath(path + Constants.DASH + file.getName());
            }
        }
        return totalFiles;
    }

}
