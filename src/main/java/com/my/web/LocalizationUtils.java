package com.my.web;

import javax.servlet.http.HttpSession;
import java.util.Locale;
import java.util.ResourceBundle;

public class LocalizationUtils {

    private LocalizationUtils() {
    }

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
}
