package vn.com.la.service.impl;

import com.google.common.io.Files;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import vn.com.la.config.ApplicationProperties;
import vn.com.la.config.Constants;
import vn.com.la.service.FileSystemHandlingService;
import vn.com.la.service.dto.LAFileDTO;
import vn.com.la.service.dto.LAFolderDTO;
import vn.com.la.service.util.LADateTimeUtil;
import vn.com.la.web.rest.vm.response.ListFileResponseVM;
import vn.com.la.web.rest.vm.response.ListFolderResponseVM;

import java.io.*;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.ZoneId;
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
        newDir.mkdir();
    }

    @Override
    public boolean deleteDirectory(String path) throws Exception {
        File oldDir = new File(rootFolder + Constants.DASH + path);
        return oldDir.delete();
    }

    @Override
    public void copy(String fromSource, String toPath, String fileName) throws Exception {
        Files.copy(new File( rootFolder + Constants.DASH +fromSource), new File(rootFolder + Constants.DASH + toPath + Constants.DASH + fileName));
    }

    @Override
    public void copy(String fromSource, String toSource) throws Exception {
        Files.copy(new File(rootFolder + Constants.DASH + fromSource), new File(rootFolder + Constants.DASH +toSource));
    }

    @Override
    public boolean move(String source, String toFolder) {
        try {
            FileUtils.moveFileToDirectory(
                FileUtils.getFile(rootFolder + Constants.DASH + source),
                FileUtils.getFile(rootFolder + Constants.DASH + toFolder), true);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    @Override
    public List<File> listFileFromPath(String path) throws Exception {
        File file = new File(rootFolder + Constants.DASH + path);
        return Arrays.asList(file.listFiles());
    }

    private List<File> walk(String path) {
        List<File> result = new ArrayList<>();
        File dir = new File(path);
        if(dir.isDirectory()) {
            File[] files = dir.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {

                    result.addAll(walk(file.getPath()));
                } else {
                    result.add(file);
                }
            }
        }
        return result;
    }

    @Override
    public List<File> listFileRecursiveFromPath(String path) throws Exception {
        return walk(rootFolder + Constants.DASH + path);
    }

    @Override
    public boolean checkFileExist(String filePath) {
        File file = new File(rootFolder + Constants.DASH + filePath);

        return file.exists();
    }

    @Override
    public ListFolderResponseVM listNfsFolderFromPath(String path) {
        if(StringUtils.isBlank(path)) {
            path = Constants.DASH;
        }
        File file = new File(rootFolder + Constants.DASH + path);
        List<LAFolderDTO> folders = new ArrayList<>();
        for(File dir: file.listFiles()){
            if(dir.isDirectory() && !dir.isHidden()) {
                LAFolderDTO laFolderDTO = new LAFolderDTO();
                laFolderDTO.setName(dir.getName());
                laFolderDTO.setRelativePath(dir.getPath().substring(rootFolder.length()));
                File[] listFiles = dir.listFiles(new FileFilter() {
                    @Override
                    public boolean accept(File f) {
                        return f.isDirectory();
                    }
                });
                laFolderDTO.setHasChild(listFiles.length > 0);
                System.out.println(dir.length());
                folders.add(laFolderDTO);
            }
        }
        ListFolderResponseVM rs = new ListFolderResponseVM();
        rs.setDirectories(folders);
        return rs;
    }

    @Override
    public ListFileResponseVM listNfsFileFromPath(String path) {
        File file = new File(rootFolder + Constants.DASH + path);
        List<LAFileDTO> fileDTOs = new ArrayList<>();
        for(File f: file.listFiles()){
            if(f.isFile() && !f.isHidden()) {
                LAFileDTO fileDTO = new LAFileDTO();
                fileDTO.setName(Files.getNameWithoutExtension(f.getName()));
                fileDTO.setType(Files.getFileExtension(f.getName()));
                fileDTO.setRelativePath(f.getPath().substring(rootFolder.length()));

                try
                {
                    BasicFileAttributes attributes = java.nio.file.Files.readAttributes(f.toPath(), BasicFileAttributes.class);
                    fileDTO.setCreatedDate(LADateTimeUtil.fileTimeToZonedDateTime(attributes.creationTime()));
                    fileDTO.setLastModifiedDate(LADateTimeUtil.fileTimeToZonedDateTime(attributes.lastModifiedTime()));
                }
                catch (IOException exception)
                {
                    System.out.println("Exception handled when trying to get file " +
                        "attributes: " + exception.getMessage());
                }
                fileDTOs.add(fileDTO);

            }
        }
        ListFileResponseVM rs = new ListFileResponseVM();
        rs.setFiles(fileDTOs);
        return rs;
    }
}
