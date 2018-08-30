package com.jrodiz.sucursalesbr.base;

import com.jrodiz.sucursalesbr.BuildConfig;

public final class AppConstants {

    private static final String PACKAGE = BuildConfig.APPLICATION_ID;

    public static final int RQST_FINE_PERMISSION = 1;
    public static final double INVALID_DOUBLE = -1;
    public static final String SUCURSAL = "S";
    public static final String CAJERO = "C";
    public static final String KEY_SUCURSAL = PACKAGE.concat(".KEY_SUCURSAL");
    public static final String KEY_LOCATION = PACKAGE.concat(".KEY_LOCATION");
    public static final int RQST_CODE_SEARCH = 100;


    private AppConstants(){}

    public static class Db {
        public static final int DB_VERSION = 1;
        public static final String DB_NAME = "database.db";
    }

    public static final class Ws {

        public static final String BASE_ENDPOINT = BuildConfig.API_BASE_URL;

        public static final String GET_SUCURSALES = "/sucursales";
        public static final String GET_LOGIN = "/login";
    }

    public static final class Errors {
        public static final int DEFAULT_ERR_CODE = -2;
    }

}
