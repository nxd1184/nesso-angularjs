package vn.com.la.service.impl;

import com.google.common.io.Files;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import vn.com.la.config.ApplicationProperties;
import vn.com.la.config.Constants;
import vn.com.la.service.FileSystemHandlingService;
import vn.com.la.service.dto.IgnoreNameDTO;
import vn.com.la.service.dto.JobTeamUserTaskDTO;
import vn.com.la.service.dto.LAFileDTO;
import vn.com.la.service.dto.LAFolderDTO;
import vn.com.la.service.util.LADateTimeUtil;
import vn.com.la.service.util.LAStringUtil;
import vn.com.la.web.rest.vm.response.ListFileResponseVM;
import vn.com.la.web.rest.vm.response.ListFolderResponseVM;

import java.io.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class FileSystemHandlingServiceImpl implements FileSystemHandlingService {
    private final ApplicationProperties applicationProperties;

    private static final List<String> ACCEPTED_EXTENSIONS = Arrays.asList("JPG", "PNG", "TIFF", "PSD", "AI", "GIF");

    private String rootFolder;

    private FileSystemHandlingServiceImpl(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
        this.rootFolder = this.applicationProperties.getRootFolder();
    }

    @Override
    public boolean validateProjectStructure(String projectCode) {

        try {

            File file = new File(rootFolder + Constants.SLASH + projectCode);
            String[] projectFolders = {Constants.BACK_LOGS, Constants.TO_DO, Constants.TO_CHECK, Constants.DONE, Constants.DELIVERY};
            if (!file.exists()) {
                file.mkdir();
                // create backlogs, todo, tocheck, done, delivery
                for (String folder : projectFolders) {
                    File projectFolderFile = new File(rootFolder + Constants.SLASH + projectCode + Constants.SLASH + folder);
                    projectFolderFile.mkdir();
                }
                return true;
            }


            for (String folder : projectFolders) {
                File projectFolder = new File(rootFolder + Constants.SLASH + projectCode + Constants.SLASH + folder);
                if (!projectFolder.exists()) {
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
    public List<String> backLogs(String projectCode) throws Exception {

        List<String> folderNames = new ArrayList<>();

        File projectFolder = new File(rootFolder + Constants.SLASH + projectCode + Constants.SLASH + Constants.BACK_LOGS);

        for (File backlogItem : projectFolder.listFiles()) {
            if (backlogItem.isDirectory()) {
                folderNames.add(backlogItem.getName());
            }
        }

        return folderNames;
    }

    @Override
    public Long countFilesFromPath(String path, List<IgnoreNameDTO> ignoreList) throws Exception {
        Long totalFiles = 0L;

        File file = new File(rootFolder + Constants.SLASH + path);
        totalFiles = new Long(walk(file.getPath(), ignoreList).size());

        return totalFiles;
    }

    @Override
    public void makeDirectory(String path) throws Exception {
        File newDir = new File(rootFolder + Constants.SLASH + path);
        newDir.mkdir();
    }

    @Override
    public boolean deleteDirectory(String path) throws Exception {
        try {
            File oldDir = new File(rootFolder + Constants.SLASH + path);
            FileUtils.deleteDirectory(oldDir);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteFile(String filePath) throws Exception {

        File oldFile = new File(rootFolder + Constants.SLASH + filePath);
        return oldFile.delete();
    }

    @Override
    public void copy(String fromSource, String toPath, String fileName) throws Exception {
        Files.copy(new File(rootFolder + Constants.SLASH + fromSource), new File(rootFolder + Constants.SLASH + toPath + Constants.SLASH + fileName));
    }

    @Override
    public void copy(String fromSource, String toSource) throws Exception {
        Files.copy(new File(rootFolder + Constants.SLASH + fromSource), new File(rootFolder + Constants.SLASH + toSource));
    }

    @Override
    public boolean move(String source, String toFolder) {
        try {
            FileUtils.moveFileToDirectory(
                FileUtils.getFile(rootFolder + Constants.SLASH + source),
                FileUtils.getFile(rootFolder + Constants.SLASH + toFolder), true);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    @Override
    public List<File> listFileFromPath(String path) throws Exception {
        File file = new File(rootFolder + Constants.SLASH + path);
        return Arrays.asList(file.listFiles());
    }

    private List<File> walk(String path, List<IgnoreNameDTO> ignoreList) {
        List<File> result = new ArrayList<>();
        File dir = new File(path);
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            for (File file : files) {

                // ignore file match ignore list
                if(ignoreList != null && ignoreList.indexOf(file.getName()) != -1) {
                    continue;
                }

                if (file.isDirectory()) {

                    result.addAll(walk(file.getPath(), ignoreList));
                } else if (!file.isHidden()) {
                    String extension = (Optional.ofNullable(FilenameUtils.getExtension(file.getName())).map(String::toString).orElse("")).toUpperCase();
                    if(ACCEPTED_EXTENSIONS.indexOf(extension) != -1) {
                        result.add(file);
                    }

                }
            }
        }
        return result;
    }

    private List<String> walkRelativeFileName(String path, List<IgnoreNameDTO> ignoreList) {
        List<String> result = new ArrayList<>();
        File dir = new File(path);
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            for (File file : files) {

                // ignore file match ignore list
                if(ignoreList != null && ignoreList.indexOf(file.getName()) != -1) {
                    continue;
                }

                if (file.isDirectory()) {
                    result.addAll(walkRelativeFileName(file.getPath(), ignoreList));
                } else if (!file.isHidden()) {
                    String extension = (Optional.ofNullable(FilenameUtils.getExtension(file.getName())).map(String::toString).orElse("")).toUpperCase();
                    if(ACCEPTED_EXTENSIONS.indexOf(extension) != -1) {
                        result.add(LAStringUtil.removeRootPath(file.getPath(), rootFolder));
                    }

                }
            }
        }
        return result;
    }

    @Override
    public List<File> listFileRecursiveFromPath(String path, List<IgnoreNameDTO> ignoreList) throws Exception {
        return walk(rootFolder + Constants.SLASH + path, ignoreList);
    }

    @Override
    public List<String> listRelativeFilePathRecursiveFromPath(String path, List<IgnoreNameDTO> ignoreList) throws Exception {
        return walkRelativeFileName(rootFolder + Constants.SLASH + path, ignoreList);
    }

    @Override
    public boolean checkFileExist(String filePath) {
        File file = new File(rootFolder + Constants.SLASH + filePath);

        return file.exists();
    }

    @Override
    public boolean checkFolderExist(String folderPath) {
        File folder = new File(rootFolder + Constants.SLASH + folderPath);
        return folder.exists();
    }

    @Override
    public ListFolderResponseVM listNfsFolderFromPath(String path) {
        if (StringUtils.isBlank(path)) {
            path = Constants.SLASH;
        }
        File file = new File(rootFolder + Constants.SLASH + path);
        List<LAFolderDTO> folders = new ArrayList<>();
        for (File dir : file.listFiles()) {
            if (dir.isDirectory() && !dir.isHidden()) {
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
//                System.out.println(dir.length());
                folders.add(laFolderDTO);
            }
        }
        ListFolderResponseVM rs = new ListFolderResponseVM();
        rs.setDirectories(folders);
        return rs;
    }

    @Override
    public ListFileResponseVM listNfsFileFromPath(String path) {
        File file = new File(rootFolder + Constants.SLASH + path);
        List<LAFileDTO> fileDTOs = new ArrayList<>();
        for (File f : file.listFiles()) {
            if (f.isFile() && !f.isHidden()) {
                LAFileDTO fileDTO = new LAFileDTO();
                fileDTO.setName(Files.getNameWithoutExtension(f.getName()));
                fileDTO.setType(Files.getFileExtension(f.getName()));
                fileDTO.setRelativePath(f.getPath().substring(rootFolder.length()));

                try {
                    BasicFileAttributes attributes = java.nio.file.Files.readAttributes(f.toPath(), BasicFileAttributes.class);
                    fileDTO.setCreatedDate(LADateTimeUtil.fileTimeToZonedDateTime(attributes.creationTime()));
                    fileDTO.setLastModifiedDate(LADateTimeUtil.fileTimeToZonedDateTime(attributes.lastModifiedTime()));
                } catch (IOException exception) {
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

    @Override
    public boolean deliverFileToDelivery(JobTeamUserTaskDTO task, String login) throws Exception {
        File fileInDone = new File((rootFolder + task.getFilePath() + task.getFileName()).replaceFirst(Constants.TO_DO, Constants.DONE));
        File folderDelivery = new File((rootFolder + task.getOriginalFilePath()).replaceFirst(Constants.BACK_LOGS, Constants.DELIVERY));
        File fileInDelivery = new File(folderDelivery, task.getOriginalFileName());

        if (!fileInDone.exists()) {
            return false;
        }

        // Create whole path in Delivery folder
        folderDelivery.mkdirs();

        Files.copy(fileInDone, fileInDelivery);

        return fileInDelivery.exists();
    }
}
