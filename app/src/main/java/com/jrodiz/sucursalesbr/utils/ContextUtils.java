package com.jrodiz.sucursalesbr.utils;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


public class ContextUtils {
    public static Parcelable retrieveParceable(@Nullable final Bundle savedInstanceState,
                                               @Nullable final Bundle arguments,
                                               @NonNull final String key) {
        if (savedInstanceState != null && savedInstanceState.containsKey(key)) {
            return savedInstanceState.getParcelable(key);
        } else if (arguments != null && arguments.containsKey(key)) {
            return arguments.getParcelable(key);
        }
        return null;
    }
}
