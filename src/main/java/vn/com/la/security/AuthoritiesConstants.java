package vn.com.la.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String USER = "ROLE_USER";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    public static final String PROJECT_MANAGER = "ROLE_PROJECT_MANAGER";

    public static final String TEAM_LEADER = "ROLE_TEAM_LEADER";

    private AuthoritiesConstants() {
    }
}
