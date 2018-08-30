package com.jrodiz.sucursalesbr.utils;

import com.jrodiz.sucursalesbr.base.AppConstants;
import com.jrodiz.sucursalesbr.base.AppUtils;

public final class ParserUtils {

    private ParserUtils() {
    }

    public static double parseDouble(final String v) {
        double d = AppConstants.INVALID_DOUBLE;
        try {
            d = Double.parseDouble(v);
        } catch (NumberFormatException e) {
            AppUtils.handleThrowable(e);
        }
        return d;
    }
}
