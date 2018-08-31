package com.jrodiz.business.ws;

import com.jrodiz.business.model.Sucursal;
import com.jrodiz.business.model.User;
import com.jrodiz.common.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface BrInterface {

    @GET(Constants.Ws.GET_SUCURSALES)
    Call<List<Sucursal>> getSucursales();

    @GET(Constants.Ws.GET_LOGIN)
    Call<User> getLogin();
}
