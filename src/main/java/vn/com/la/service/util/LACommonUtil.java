package vn.com.la.service.util;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class LACommonUtil {
    public static String decode(String value) {
        try {
            return URLDecoder.decode(value, StandardCharsets.UTF_8.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return "";
    }
}
