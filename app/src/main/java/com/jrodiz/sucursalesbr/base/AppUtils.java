package com.jrodiz.sucursalesbr.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.jrodiz.sucursalesbr.R;

public class AppUtils {
    public static <T extends Throwable> void handleThrowable(@NonNull final T ex) {
        ex.printStackTrace();
    }

    public static void printNoNetwork(Context context) {
        Toast.makeText(context, context.getResources().getString(R.string.no_network), Toast.LENGTH_SHORT).show();
    }
}
