package com.liferay.sales.engineering.pulse.util;

public final class StringUtils {

    public static boolean isBlank(String string) {
        return string == null || string.isBlank();
    }

    public static boolean isNotBlank(String string) {
        return !isBlank(string);
    }

}
