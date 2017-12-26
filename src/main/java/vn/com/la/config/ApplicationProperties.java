package vn.com.la.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import vn.com.la.config.audit.FtpProperties;

/**
 * Properties specific to JHipster.
 * <p>
 * Properties are configured in the application.yml file.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private FtpProperties ftpProperties;

    public FtpProperties getFtpProperties() {
        return ftpProperties;
    }

    public void setFtpProperties(FtpProperties ftpProperties) {
        this.ftpProperties = ftpProperties;
    }
}
