package vn.com.la.config;

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

    public static final Long ZERO = 0L;

    private Constants() {
    }
}
