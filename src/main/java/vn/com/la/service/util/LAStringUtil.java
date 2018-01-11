package vn.com.la.service.util;

import vn.com.la.config.Constants;

public class LAStringUtil {

    public static String buildFolderPath(String ...paths) {
        StringBuilder pathBuilder = new StringBuilder();
        for(String path: paths) {
            pathBuilder.append(path).append(Constants.DASH);
        }
        return pathBuilder.toString();
    }
}
