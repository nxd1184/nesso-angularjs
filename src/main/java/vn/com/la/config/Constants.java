package vn.com.la.config;

import io.swagger.models.auth.In;
import vn.com.la.domain.enumeration.FileStatusEnum;

import java.util.Arrays;
import java.util.List;

/**
 * Application constants.
 */
public final class Constants {

    //Regex for acceptable logins
    public static final String LOGIN_REGEX = "^[_'.@A-Za-z0-9-]*$";

    public static final String SYSTEM_ACCOUNT = "system";
    public static final String ANONYMOUS_USER = "anonymoususer";
    public static final String DEFAULT_LANGUAGE = "en";

    public static final String BACK_LOGS = "BackLogs";
    public static final String TO_DO = "To do";
    public static final String TO_CHECK = "To check";
    public static final String DONE = "Done";
    public static final String DELIVERY = "Delivery";

    public static final String DASH = "/";
    public static final String UNDERSCORE = "_";

    public static final Integer ZERO = 0;
    public static final Integer ONE = 1;

    public static final String NESSO_GLOBAL_SEQUENCE = "SQ_NESSO_GLOBAL_SEQUENCE";

    public static final List<FileStatusEnum> TO_DO_STATUS_LIST = Arrays.asList(FileStatusEnum.TODO, FileStatusEnum.REWORK);
    public static final List<FileStatusEnum> TO_CHECK_STATUS_LIST = Arrays.asList(FileStatusEnum.TOCHECK);
    public static final List<FileStatusEnum> DONE_STATUS_LIST = Arrays.asList(FileStatusEnum.DONE);
    public static final List<FileStatusEnum> REWORK_STATUS_LIST = Arrays.asList(FileStatusEnum.REWORK);

    private Constants() {
    }
}
