package com.jrodiz.business.utils;

import android.support.annotation.NonNull;

public class AppUtils {
    public static <T extends Throwable> void handleThrowable(@NonNull final T ex) {
        ex.printStackTrace();
    }
}
