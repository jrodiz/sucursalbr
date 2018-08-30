package com.jrodiz.sucursalesbr.ws.manager;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.jrodiz.sucursalesbr.base.AppConstants;
import com.jrodiz.sucursalesbr.base.BrSucursales;
import com.jrodiz.sucursalesbr.ctrl.SucursalCtrl;
import com.jrodiz.sucursalesbr.db.AppLocalDb;
import com.jrodiz.sucursalesbr.obj.Sucursal;
import com.readystatesoftware.chuck.ChuckInterceptor;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public enum RetrofitManager {
    MANAGER;

    private static BrInterface mService = null;

    public static RetrofitManager get() {
        return MANAGER;
    }

    public static class RequestException extends Exception {
        RequestException(@Nullable final String eMsg) {
            super(eMsg);
        }
    }


    private static BrInterface getConnection() {
        if (mService == null) {

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new ChuckInterceptor(BrSucursales.getContext()))
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(AppConstants.Ws.BASE_ENDPOINT)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            mService = retrofit.create(BrInterface.class);
        }
        return mService;
    }

    public void requestSucursales(@NonNull final GenericHandler<List<Sucursal>> handler) {
        Call<List<Sucursal>> call = getConnection().getSucursales();
        call.enqueue(handler);
    }
}
