package com.jrodiz.common;


public final class ParserUtils {

    private ParserUtils() {
    }

    public static double parseDouble(final String v) {
        double d = Constants.INVALID_DOUBLE;
        try {
            d = Double.parseDouble(v);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return d;
    }
}
