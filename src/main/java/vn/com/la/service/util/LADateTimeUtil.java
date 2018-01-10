package vn.com.la.service.util;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;

public class LADateTimeUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(LADateTimeUtil.class.getName());

    public static DateTime INFINITE_DATE = new DateTime(2999, 12, 31, 0, 0, 0, 0, DateTimeZone.UTC);
    public static DateTime FAR_PAST_DATE = new DateTime(1970, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC);

    // example: 2016-05-13 03:18:28:000 +0700
    public static final String DATETIME_ISO_FORMAT = "yyyy-MM-dd HH:mm:ss:SSS Z";
    public static final DateTimeFormatter DATETIME_ISO_FORMATTER = DateTimeFormat.forPattern(DATETIME_ISO_FORMAT);
    public static final DateTimeFormatter FLIGHT_DATETIME_ISO_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm Z");

    public static final String DATE_ISO_FORMAT = "yyyy-MM-dd";
    private static final DateTimeFormatter DATE_ISO_FORMATTER = DateTimeFormat.forPattern(DATE_ISO_FORMAT);

    private static final String GENERALIZED_TIME_FORMAT = "yyyyMMddHHmmssZ";
    private static final DateTimeFormatter GENERALIZED_TIME_FORMATTER = DateTimeFormat.forPattern(GENERALIZED_TIME_FORMAT);

    // example: 03:18, 19:25
    private static final String TIME_ISO_FORMAT = "HH:mm";
    private static final DateTimeFormatter TIME_ISO_FORMATTER = DateTimeFormat.forPattern(TIME_ISO_FORMAT);

    // format 2016-03-30T16:55:00.000+05:30
    private static final DateTimeFormatter JODA_ISO_DATETIME_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZZ");
    private static final DateTimeFormatter DATETIME_ISO8601_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss");
    private static final DateTimeFormatter DATE_ISO8601_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd");

    public static ZonedDateTime isoStringToZonedDateTime(final String isoDateTimeStr) {
        if (StringUtils.isBlank(isoDateTimeStr)) {
            return null;
        }
        return DATETIME_ISO_FORMATTER.parseDateTime(isoDateTimeStr).toGregorianCalendar().toZonedDateTime();
    }


}
