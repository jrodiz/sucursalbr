package com.jrodiz.sucursalesbr.base;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;

import com.jrodiz.sucursalesbr.db.AppLocalDb;
import com.jrodiz.sucursalesbr.db.SucursalDao;

public class BaseController {

    public enum DbOpt {
        UNKNOWN(null), GET_ALL_SUCURSAL(null), INSERT_SUCURSALS(null);

        private Object data;

        DbOpt(Object data) {
            this.data = data;
        }

        public Object getData() {
            return data;
        }

        public DbOpt setData(Object data) {
            this.data = data;
            return this;
        }
    }

    protected SucursalDao getDb(@NonNull final Context context) {
        return AppLocalDb.getInstance(context.getApplicationContext()).getSucursalDao();
    }

    protected boolean isNetworkConnected(@NonNull final Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm != null) {
            return cm.getActiveNetworkInfo() != null;
        }

        return false;
    }
}
