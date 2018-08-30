package com.jrodiz.sucursalesbr.base;

import android.app.Application;
import android.content.Context;

import com.jrodiz.sucursalesbr.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class BrSucursales extends Application {
    public static BrSucursales mApp = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/bold.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

    public static Context getContext() {
        return mApp;
    }
}
