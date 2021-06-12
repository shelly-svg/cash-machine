package com.my.web.localization;

import javax.servlet.http.HttpSession;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Localization utility class
 *
 * @author Denys Tsebro
 */
public class LocalizationUtils {

    private LocalizationUtils() {
    }

    /**
     * Get rb based on user locale language, stored at session
     *
     * @param session session
     * @return Resource bundle
     */
    public static ResourceBundle getCurrentRb(HttpSession session) {

        String localeName = "en";
        Object localeObj = session.getAttribute("lang");
        if (localeObj != null) {
            localeName = localeObj.toString();
        }

        Locale locale;
        if ("ru".equals(localeName)) {
            locale = new Locale("ru", "RU");
        } else {
            locale = new Locale("en", "EN");
        }
        return ResourceBundle.getBundle("resources", locale);
    }

    public static ResourceBundle getEnglishRb() {
        Locale locale = new Locale("en", "EN");
        return ResourceBundle.getBundle("resources", locale);
    }

}
