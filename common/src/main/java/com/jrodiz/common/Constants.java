package com.jrodiz.common;

public final class Constants {
    public static final double INVALID_DOUBLE = -1;
    public static final String SUCURSAL = "S";
    public static final String CAJERO = "C";

    private Constants (){}

    public static final class Ws {

        public static final String BASE_ENDPOINT = "http://json.banregio.io";

        public static final String GET_SUCURSALES = "/sucursales";
        public static final String GET_LOGIN = "/login";
    }

    public static class Db {
        public static final int DB_VERSION = 1;
        public static final String DB_NAME = "database.db";
    }

    public static final class Errors {
        public static final int DEFAULT_ERR_CODE = -2;
        public static final int NO_NETWORK = -3;
        public static final int WRONG_CREDENTIALS = -4;
    }
}
