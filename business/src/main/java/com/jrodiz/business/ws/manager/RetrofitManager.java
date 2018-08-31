package com.jrodiz.business.ws.manager;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.jrodiz.business.model.Sucursal;
import com.jrodiz.business.model.User;
import com.jrodiz.business.ws.BrInterface;
import com.jrodiz.common.Constants;

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
        public RequestException(@Nullable final String eMsg) {
            super(eMsg);
        }
    }


    private static BrInterface getConnection() {
        if (mService == null) {

            OkHttpClient client = new OkHttpClient.Builder()
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(Constants.Ws.BASE_ENDPOINT)
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
    public void requestLogin(@NonNull final GenericHandler<User> handler) {
        Call<User> call = getConnection().getLogin();
        call.enqueue(handler);
    }
}
