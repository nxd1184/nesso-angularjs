package vn.com.la.service.util;

import vn.com.la.config.Constants;

public class LAStringUtil {

    public static String buildFolderPath(String ...paths) {
        StringBuilder pathBuilder = new StringBuilder();
        for(String path: paths) {
            pathBuilder.append(path).append(Constants.SLASH);
        }
        return pathBuilder.toString();
    }

    public static String removeRootPath(String fullPath, String rootPath) {
        return fullPath.substring(rootPath.length());
    }
}
